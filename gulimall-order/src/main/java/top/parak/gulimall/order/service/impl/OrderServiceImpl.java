package top.parak.gulimall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import top.parak.gulimall.common.cosntant.OrderConstant;
import top.parak.gulimall.common.enume.OrderStatusEnum;
import top.parak.gulimall.common.to.SkuHasStockVo;
import top.parak.gulimall.common.to.mq.OrderTo;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.common.vo.MemberResponseVo;
import top.parak.gulimall.order.config.GulimallRabbitConfig;
import top.parak.gulimall.order.dao.OrderDao;
import top.parak.gulimall.order.entity.OrderEntity;
import top.parak.gulimall.order.entity.OrderItemEntity;
import top.parak.gulimall.order.feign.CartFeignService;
import top.parak.gulimall.order.feign.MemberFeignService;
import top.parak.gulimall.order.feign.ProductFeignService;
import top.parak.gulimall.order.feign.WareFeignService;
import top.parak.gulimall.order.interceptor.LoginUserInterceptor;
import top.parak.gulimall.order.service.OrderItemService;
import top.parak.gulimall.order.service.OrderService;
import top.parak.gulimall.order.to.OrderCreateTo;
import top.parak.gulimall.order.vo.*;

/**
 * 订单
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Service("orderService")
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    private ThreadLocal<OrderSubmitVo> orderSubmitVoThreadLocal = new ThreadLocal<>();

    @Autowired
    private MemberFeignService memberFeignService;

    @Autowired
    private CartFeignService cartFeignService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private GulimallRabbitConfig.GulimallRabbitOrderProperties rabbitOrderProperties;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberResponseVo memberResponseVo = LoginUserInterceptor.threadLocal.get();

        // 注意，这里使用Feign远程调用，查询购物车
        // （1）Feign构造请求的时候，不会带上请求头
        // 那么，购物车服务拦截器就无法获取到cookie
        // 因此，需要配置Feign拦截器带上cookie
        // （2）Feign在异步情况下会丢失请求上下文
        // RequestContextHolder内部使用ThreadLocal存储数据
        // 多线程情况下无法或许其他线程的ThreadLocal数据
        // 所以要给每个异步线程都要设置请求上下文

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        // 🚀 异步编排

        // 1. 查询收货地址列表
        CompletableFuture<Void> getAddressFuture = CompletableFuture.runAsync(() -> {
            try {
                RequestContextHolder.setRequestAttributes(attributes);
                log.debug("get address future [Thread Id: {}]", Thread.currentThread().getId());
                List<MemberReceiveAddressVo> address = memberFeignService.getAddress(memberResponseVo.getId());
                orderConfirmVo.setAddress(address);
            } catch (Exception e) {
                log.warn("远程查询地址列表失败：[用户服务可能未启动]");
            }
        }, executor);

        // 2. 查询选中购物项
        CompletableFuture<Void> getCartItemsFuture = CompletableFuture.runAsync(() -> {
            try {
                RequestContextHolder.setRequestAttributes(attributes);
                log.debug("get cart items [Thread Id: {}]", Thread.currentThread().getId());
                List<OrderItemVo> items = cartFeignService.getCurrentUserCartItems();
                orderConfirmVo.setItems(items);
            } catch (Exception e) {
                log.warn("远程查询购物项失败：[购物车服务可能未启动]");
            }
        }, executor).thenRunAsync(() -> {
            // 查询是否有库存
            List<OrderItemVo> items = orderConfirmVo.getItems();
            List<Long> skuIdList = items.stream().map(OrderItemVo::getSkuId)
                    .collect(Collectors.toList());
            R r = wareFeignService.getSkuHasStock(skuIdList);
            List<SkuHasStockVo> data = r.getData(new TypeReference<List<SkuHasStockVo>>() { });
            if (!CollectionUtils.isEmpty(data)) {
                Map<Long, Boolean> stocks = data.stream()
                        .collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
                orderConfirmVo.setStocks(stocks);
            }
        }, executor);

        // 3. 查询用户积分
        Integer integration = memberResponseVo.getIntegration();
        orderConfirmVo.setIntegration(integration);

        // 4. 幂等性，防重令牌
        String orderToken = UUID.randomUUID().toString().replace("-", "");
        orderConfirmVo.setOrderToken(orderToken);
        redisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId(),
                orderToken, 30, TimeUnit.MINUTES);

        CompletableFuture.allOf(getAddressFuture, getCartItemsFuture).get();
        return orderConfirmVo;
    }

    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {
        // 设置线程共享变量
        orderSubmitVoThreadLocal.set(orderSubmitVo);

        // 设置订单状态
        SubmitOrderResponseVo submitOrderResponseVo = new SubmitOrderResponseVo();
        submitOrderResponseVo.setCode(0);

        // 验证令牌
        String orderToken = orderSubmitVo.getOrderToken();
        MemberResponseVo memberResponseVo = LoginUserInterceptor.threadLocal.get();
        String userTokenKey = OrderConstant.USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId();

        // 验证和删除的过程必须保证原子性
        // 💠 使用lua脚本
        String luaScript =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "    return redis.call('del', KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end";

        // 1代表删除成功。0代表删除失败
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(luaScript, Long.class),
                Collections.singletonList(userTokenKey), orderToken);

        // 验证不通过
        if (ObjectUtils.isEmpty(result) || result.intValue() == 0) {
            submitOrderResponseVo.setCode(1);
            return submitOrderResponseVo;
        }

        // 验证通过
        if (result.intValue() == 1) {
            // 1. 创建订单
            OrderCreateTo order = createOrder();

            // 2. 验价
            BigDecimal currentPayAmount = order.getOrder().getPayAmount();
            BigDecimal submitPayAmount= orderSubmitVo.getPayPrice();
            double deltaAmount = Math.abs(currentPayAmount.subtract(submitPayAmount).doubleValue());

            if (deltaAmount < 0.01) { // 验价成功
                // 3. 保存订单
                saveOrder(order);

                // 4. 锁定库存
                WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
                String orderSn = order.getOrder().getOrderSn();
                wareSkuLockVo.setOrderSn(orderSn);
                List<OrderItemVo> locks = order.getOrderItems().stream().map(item -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(item.getSkuId());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setTitle(item.getSkuName());
                    return orderItemVo;
                }).collect(Collectors.toList());
                wareSkuLockVo.setLocks(locks);

                R r = null;
                try {
                    r = wareFeignService.orderLockStock(wareSkuLockVo);
                    if (r.getCode() == 0) { // 锁定成功
                        submitOrderResponseVo.setOrderEntity(order.getOrder());

                        log.info("订单[订单号={}]锁定库存成功，向MQ发送创建订单消息：{}", orderSn, order.getOrder());
                        rabbitTemplate.convertAndSend(rabbitOrderProperties.getEventExchange(),
                                rabbitOrderProperties.getCreateOrderRoutingKey(), order.getOrder());

                        return submitOrderResponseVo;
                    }
                } catch (Exception e) {
                    log.warn("远程锁定商品库存失败：[库存服务可能未启动]");
                }

                // 走到这里，说明锁定库存失败
                submitOrderResponseVo.setCode(3);
                return submitOrderResponseVo;
            } else { // 验价失败
                submitOrderResponseVo.setCode(2);
                return submitOrderResponseVo;
            }
        }

        return submitOrderResponseVo;
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        OrderEntity orderEntity = this.getOne(
                new QueryWrapper<OrderEntity>()
                .eq("order_sn", orderSn)
        );

        return orderEntity;
    }

    @Override
    public void closeOrder(OrderEntity orderEntity) {
        // 查询订单的最新状态
        Long id = orderEntity.getId();
        OrderEntity orderInDb = this.getById(id);
        if (orderInDb.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())) {
            // 更新订单状态
            OrderEntity orderForUpdate = new OrderEntity();
            orderForUpdate.setId(id);
            orderForUpdate.setStatus(OrderStatusEnum.CANCELED.getCode());
            this.updateById(orderForUpdate);

            // 向MQ发送消息，保证100%发送
            // TODO: DB保持每个消息的详细信息，定时任务扫描DB，将发送失败的消息再发送一次
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(orderInDb, orderTo);
            try {
                log.info("订单[订单号={}]关闭，向MQ发送解锁库存消息：{}", orderInDb.getOrderSn(), orderTo);
                rabbitTemplate.convertAndSend(rabbitOrderProperties.getEventExchange(),
                        rabbitOrderProperties.getReleaseOtherRoutingKey(), orderTo);
            } catch (AmqpException e) {
                // TODO: 发送失败，进行重试
            }
        }
    }

    /**
     * 创建订单
     */
    private OrderCreateTo createOrder() {
        // 1. 生成订单号
        String orderSn = IdWorker.getTimeId();
        OrderEntity orderEntity = buildOrder(orderSn);

        // 2. 获取订单项
        List<OrderItemEntity> items = buildOrderItems(orderSn);

        // 3. 验价
        computerPrice(orderEntity, items);

        OrderCreateTo orderCreateTo = new OrderCreateTo();
        orderCreateTo.setOrder(orderEntity);
        orderCreateTo.setOrderItems(items);
        return orderCreateTo;
    }

    /**
     * 构建订单
     */
    private OrderEntity buildOrder(String orderSn) {
        OrderEntity orderEntity = new OrderEntity();

        // 1. 设置订单基本信息
        orderEntity.setOrderSn(orderSn);
        orderEntity.setCreateTime(new Date());
        orderEntity.setCommentTime(new Date());
        orderEntity.setReceiveTime(new Date());
        orderEntity.setDeliveryTime(new Date());

        // 2. 设置用户基本信息
        MemberResponseVo member = LoginUserInterceptor.threadLocal.get();
        orderEntity.setMemberId(member.getId());
        orderEntity.setMemberUsername(member.getUsername());
        orderEntity.setBillReceiverEmail(member.getEmail());

        // 3. 设置收货地址信息
        OrderSubmitVo orderSubmitVo = orderSubmitVoThreadLocal.get();
        R r = null;
        try {
            r = wareFeignService.getFare(orderSubmitVo.getAddrId());
            if (!ObjectUtils.isEmpty(r) && r.getCode() == 0) {
                FareVo fareVo = r.getData(new TypeReference<FareVo>() { });
                orderEntity.setFreightAmount(fareVo.getFare());
                orderEntity.setReceiverCity(fareVo.getAddress().getCity());
                orderEntity.setReceiverDetailAddress(fareVo.getAddress().getDetailAddress());
                orderEntity.setReceiverPhone(fareVo.getAddress().getPhone());
                orderEntity.setReceiverName(fareVo.getAddress().getName());
                orderEntity.setReceiverPostCode(fareVo.getAddress().getPostCode());
                orderEntity.setReceiverProvince(fareVo.getAddress().getProvince());
                orderEntity.setReceiverRegion(fareVo.getAddress().getRegion());
            } else {
                r = null;
            }
        } catch (Exception e) {
            r = null;
        }
        if (ObjectUtils.isEmpty(r)) {
            log.warn("远程查询运费信息失败：[仓储服务可能未启动]");
            throw new RuntimeException("远程查询运费信息失败");
        }

        // 4. 设置订单状态
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setDeleteStatus(0);
        orderEntity.setAutoConfirmDay(7);

        return orderEntity;
    }

    /**
     * 构建订单项列表
     */
    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        List<OrderItemVo> cartItems = cartFeignService.getCurrentUserCartItems();
        List<OrderItemEntity> itemEntities = null;

        if (!CollectionUtils.isEmpty(cartItems)) {
            itemEntities = cartItems.stream().map(cartItem -> {
                OrderItemEntity itemEntity = buildOrderItem(cartItem);
                itemEntity.setOrderSn(orderSn);
                return itemEntity;
            }).collect(Collectors.toList());
        }

        return itemEntities;
    }

    /**
     * 构建订单项
     */
    private OrderItemEntity buildOrderItem(OrderItemVo cartItem) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();

        // 1. 商品的SPU信息
        Long skuId = cartItem.getSkuId();
        R r = null;
        try {
            r = productFeignService.getSpuInfoBySkuId(skuId);
            if (!ObjectUtils.isEmpty(r) && r.getCode() == 0) {
                SpuInfoVo spuInfoVo = r.getData(new TypeReference<SpuInfoVo>() { });
                orderItemEntity.setSpuId(spuInfoVo.getId());
                orderItemEntity.setSpuName(spuInfoVo.getSpuName());
                orderItemEntity.setSpuBrand(spuInfoVo.getBrandId().toString());
                orderItemEntity.setCategoryId(spuInfoVo.getCatalogId());
            } else {
                r = null;
            }
        } catch (Exception e) {
            r = null;
        }
        if (ObjectUtils.isEmpty(r)) {
            log.warn("远程查询SPU信息失败：[商品服务可能未启动]");
            throw new RuntimeException("远程查询SPU信息失败");
        }

        // 2. 商品的SKU信息
        orderItemEntity.setSkuId(cartItem.getSkuId());
        orderItemEntity.setSkuName(cartItem.getTitle());
        orderItemEntity.setSkuPrice(cartItem.getPrice());
        orderItemEntity.setSkuPic(cartItem.getImage());
        String skuAttrStr = StringUtils.collectionToDelimitedString(cartItem.getSkuAttr(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttrStr);
        orderItemEntity.setSkuQuantity(cartItem.getCount());

        // 3. 商品的积分信息
        int growth = cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount())).intValue();
        orderItemEntity.setGiftGrowth(growth);
        orderItemEntity.setGiftIntegration(growth);

        // 4. 订单的价格信息
        orderItemEntity.setPromotionAmount(new BigDecimal("0.0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0.0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0.0"));

        // 5. 计算订单项价格
        // 原始价格
        BigDecimal originAmount = orderItemEntity.getSkuPrice()
                .multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        // 减去积分抵扣和优惠金额
        BigDecimal realAmount = originAmount.subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getPromotionAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(realAmount);

        return orderItemEntity;
    }

    /**
     * 验价
     */
    private void computerPrice(OrderEntity orderEntity, List<OrderItemEntity> items) {
        // 订单应付总额
        BigDecimal totalAmount = new BigDecimal("0.0");
        // 优惠总额
        BigDecimal coupon = new BigDecimal("0.0");
        // 积分抵扣总额
        BigDecimal integration = new BigDecimal("0.0");
        // 促销总额
        BigDecimal promotion = new BigDecimal("0.0");
        // 积分
        BigDecimal gift = new BigDecimal("0.0");
        // 成长值
        BigDecimal growth = new BigDecimal("0.0");

        // 叠加每一个购物项
        for (OrderItemEntity item : items) {
            totalAmount = totalAmount.add(item.getRealAmount());
            coupon = coupon.add(item.getCouponAmount());
            integration = integration.add(item.getIntegrationAmount());
            promotion = promotion.add(item.getPromotionAmount());
            gift.add(new BigDecimal(item.getGiftIntegration().toString()));
            growth.add(new BigDecimal(item.getGiftGrowth().toString()));
        }

        // 设置金额信息
        orderEntity.setTotalAmount(totalAmount);
        orderEntity.setPayAmount(totalAmount.add(orderEntity.getFreightAmount()));
        orderEntity.setCouponAmount(coupon);
        orderEntity.setIntegrationAmount(integration);
        orderEntity.setPromotionAmount(promotion);

        // 设置成长信息
        orderEntity.setIntegration(gift.intValue());
        orderEntity.setGrowth(growth.intValue());

        // 设置状态信息
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setDeleteStatus(0);
    }

    /**
     * 保存订单
     */
    private void saveOrder(OrderCreateTo order) {
        OrderEntity orderEntity = order.getOrder();
        orderEntity.setModifyTime(new Date());
        this.save(orderEntity);

        List<OrderItemEntity> orderItems = order.getOrderItems();
        orderItems = orderItems.stream().map(item -> {
            item.setOrderId(orderEntity.getId());
            item.setSpuName(item.getSpuName());
            item.setOrderSn(orderEntity.getOrderSn());
            return item;
        }).collect(Collectors.toList());
        orderItemService.saveBatch(orderItems);
    }

}
