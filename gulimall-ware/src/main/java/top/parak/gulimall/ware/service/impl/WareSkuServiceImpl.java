package top.parak.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.parak.gulimall.common.enume.OrderStatusEnum;
import top.parak.gulimall.common.to.mq.OrderTo;
import top.parak.gulimall.common.to.mq.StockDetailTo;
import top.parak.gulimall.common.to.mq.StockLockedTo;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.ware.config.GulimallRabbitConfig;
import top.parak.gulimall.ware.dao.WareSkuDao;
import top.parak.gulimall.ware.entity.WareOrderTaskDetailEntity;
import top.parak.gulimall.ware.entity.WareOrderTaskEntity;
import top.parak.gulimall.ware.entity.WareSkuEntity;
import top.parak.gulimall.common.exception.NoStockException;
import top.parak.gulimall.ware.feign.OrderFeignService;
import top.parak.gulimall.ware.feign.ProductFeignService;
import top.parak.gulimall.ware.service.WareOrderTaskDetailService;
import top.parak.gulimall.ware.service.WareOrderTaskService;
import top.parak.gulimall.ware.service.WareSkuService;
import top.parak.gulimall.common.to.SkuHasStockVo;
import top.parak.gulimall.ware.vo.OrderItemVo;
import top.parak.gulimall.ware.vo.OrderVo;
import top.parak.gulimall.ware.vo.WareSkuLockVo;

/**
 * 商品库存
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Service("wareSkuService")
@Slf4j
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuDao wareSkuDao;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private WareOrderTaskService wareOrderTaskService;

    @Autowired
    private OrderFeignService orderFeignService;

    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private GulimallRabbitConfig.GulimallRabbitStockProperties rabbitStockProperties;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();

        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            queryWrapper.eq("sku_id", skuId);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        // 判断如果还没有这个库存记录是否存在
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(
                new QueryWrapper<WareSkuEntity>()
                        .eq("sku_id", skuId)
                        .eq("ware_id", wareId)
        );

        if (CollectionUtils.isEmpty(wareSkuEntities)) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);

            // 远程查询sku的名字
            // 如果失败，整个事务不需要回滚
            // 1. catch异常
            // 2. TODO: 服务降级/熔断
            try {
                R info = productFeignService.info(skuId);
                Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");
                if (info.getCode() == 0) {
                    wareSkuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {
                log.error("【远程调用】 远程查询SKU名字失败：[商品服务可能未启动或者已宕机]");
            }

            wareSkuEntity.setSkuName("");
            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> vos = skuIds.stream().map(skuId -> {
            Long count = this.baseMapper.getSkuStock(skuId);

            SkuHasStockVo vo = new SkuHasStockVo();
            vo.setSkuId(skuId);
            vo.setHasStock(count != null && count > 0);
            return vo;
        }).collect(Collectors.toList());

        return vos;
    }

    /**
     * <b>库存解锁的场景</b>
     * <ol>
     * <li>下订单成功，订单过期没有支付被系统自动取消、被用户手动取消</li>
     * <li>下订单成功，库存锁定成功，接下来的业务调用失败，导致订单回滚，之前锁定的库存就需要解锁</li>
     * </ol>
     */
    @Transactional(rollbackFor = { NoStockException.class },
            isolation = Isolation.REPEATABLE_READ,
            propagation = Propagation.REQUIRED
    )
    @Override
    public Boolean orderLockStock(WareSkuLockVo wareSkuLockVo) {
        // 锁定库存之前
        // 先保存订单
        // 以便回滚
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(wareSkuLockVo.getOrderSn());
        wareOrderTaskService.save(wareOrderTaskEntity);

        // [理论上]
        // 1. 按照下单的收货地址，找到一个就近仓库，锁定库存
        // [实际上]
        // 1. 遍历仓库锁，哪个仓库有对应的SKU库存，就锁哪个

        List<OrderItemVo> locks = wareSkuLockVo.getLocks();
        List<SkuWareHasStock> skuWareHasStocks = locks.stream().map(item -> {
            SkuWareHasStock skuWareHasStock = new SkuWareHasStock();

            Long skuId = item.getSkuId();
            skuWareHasStock.setSkuId(skuId);
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            skuWareHasStock.setWareId(wareIds);
            skuWareHasStock.setNum(item.getCount());

            return skuWareHasStock;
        }).collect(Collectors.toList());

        // 2. 锁定库存

        for (SkuWareHasStock skuWareHasStock : skuWareHasStocks) {
            Boolean skuStocked = Boolean.FALSE;

            Long skuId = skuWareHasStock.getSkuId();
            List<Long> wareIds = skuWareHasStock.getWareId();

            if (CollectionUtils.isEmpty(wareIds)) {
                throw new NoStockException(skuId);
            }

            // 1. 如果每一个库存都锁定成功，将当前商品锁定了几件的工作单记录发送给MQ
            // 2. 如果锁定失败，前面保存的工作单信息就回滚。发行出去的消息，即时要解锁记录，由于去数据库查不到记录
            for (Long wareId : wareIds) {
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, skuWareHasStock.getNum());

                if (count == 1) { // 锁定成功
                    WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity(null,
                            skuId, "", skuWareHasStock.getNum(),
                            wareOrderTaskEntity.getId(), wareId, 1
                    );
                    wareOrderTaskDetailService.save(wareOrderTaskDetailEntity);
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setId(wareOrderTaskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(wareOrderTaskDetailEntity, stockDetailTo);
                    stockLockedTo.setDetailTo(stockDetailTo);

                    log.info("【订单锁库】 订单[订单号={}]成功锁定库存: {}", wareSkuLockVo.getOrderSn(), wareSkuLockVo.getLocks());
                    rabbitTemplate.convertAndSend(rabbitStockProperties.getEventExchange(),
                            rabbitStockProperties.getLockStockRoutingKey(), stockLockedTo);

                    skuStocked = true;
                    break;
                } else {
                    // 当前仓库锁库存失败，重试下一个仓库
                }
            }

            if (!skuStocked) {
                throw new NoStockException(skuId);
            }
        }

        return Boolean.TRUE;
    }

    @Override
    public void unlockStock(StockLockedTo stockLockedTo) {
        Long id = stockLockedTo.getId();
        StockDetailTo detail = stockLockedTo.getDetailTo();
        Long detailId = detail.getId();

        // 解锁逻辑
        // 1 查询数据库关于这个订单的锁定库存信息
        // 1.1 如果 有：证明库存锁定成功，至于要不要解锁要看订单情况
        // 1.1.1 如果 没有这个订单，必须解锁
        // 1.1.2 如果 有这个订单，那么就要看订单状态
        //       已取消 -> 必须解锁，未取消 -> 不能解锁
        // 1.2 如果 没有：库存锁定失败，库存回滚了，这种情况无需解锁

        WareOrderTaskDetailEntity taskDetailEntity = wareOrderTaskDetailService.getById(detailId);
        if (!ObjectUtils.isEmpty(taskDetailEntity)) {
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(id);
            String orderSn = taskEntity.getOrderSn();

            R r = null;
            try {
                r = orderFeignService.getOrderStatus(orderSn);
            } catch (Exception e) {
                log.error("【远程调用】 远程查询订单信息失败：[订单服务可能未启动或者已宕机]");
                throw new RuntimeException("远程查询订单信息失败");
            }

            if (!ObjectUtils.isEmpty(r) && r.getCode() == 0) {
                OrderVo orderVo = r.getData(new TypeReference<OrderVo>() { });
                if (ObjectUtils.isEmpty(orderVo) || orderVo.getStatus().equals(OrderStatusEnum.CANCELED.getCode())) {
                    // 订单不存在或者订单已取消，执行解锁
                    if (taskDetailEntity.getLockStatus() == 1) {
                        updateStockData(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId);
                    }
                }
            }
        }
        // else 无需解锁
    }

    @Override
    public void unlockStock(OrderTo orderTo) {
        String orderSn = orderTo.getOrderSn();
        // 查询库存工作单的最新状态
        WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        Long id = wareOrderTaskEntity.getId();
        // 按照工作单找到没有解锁的库存进行解锁
        List<WareOrderTaskDetailEntity> list = wareOrderTaskDetailService.list(
                new QueryWrapper<WareOrderTaskDetailEntity>()
                        .eq("task_id", id)
                        .eq("lock_status", 1)
        );
        list.forEach(entity -> {
            updateStockData(entity.getSkuId(), entity.getWareId(), entity.getSkuNum(), entity.getId());
        });
    }

    /**
     * 解锁库存，更细库存工作单信息
     * @param skuId         SKU ID
     * @param wareId        库存ID
     * @param num           解锁数量
     * @param taskDetailId  工作单详情ID
     */
    private void updateStockData(Long skuId, Long wareId, Integer num, Long taskDetailId) {
        // 更新库存
        wareSkuDao.unlockStock(skuId, wareId, num);
        // 更新库存工作单的状态
        WareOrderTaskDetailEntity taskDetailEntity = new WareOrderTaskDetailEntity();
        taskDetailEntity.setId(taskDetailId);
        taskDetailEntity.setLockStatus(2); // 已解锁
        wareOrderTaskDetailService.updateById(taskDetailEntity);
    }

    @Data
    static class SkuWareHasStock {

        private Long skuId;

        private List<Long> wareId;

        private Integer num;

    }

}
