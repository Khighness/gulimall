package top.parak.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.parak.gulimall.cart.feign.ProductFeignService;
import top.parak.gulimall.cart.interceptor.CartInterceptor;
import top.parak.gulimall.cart.service.CartService;
import top.parak.gulimall.cart.vo.Cart;
import top.parak.gulimall.cart.vo.CartItem;
import top.parak.gulimall.cart.vo.SkuInfoVo;
import top.parak.gulimall.cart.vo.UserInfoTo;
import top.parak.gulimall.common.cosntant.CartConstant;
import top.parak.gulimall.common.utils.R;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * @author KHighness
 * @since 2022-01-03
 * @email parakovo@gmail.com
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(getCartKey());

        // 检查购物车中是否已存在该商品
        String result = (String) cartOps.get(skuId.toString());

        // 添加新商品到购物车
        if (StringUtils.isEmpty(result)) {
            CartItem cartItem = new CartItem();

            // 🚀 异步编排

            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                // 1。 远程查询当前要添加的商品信息
                R skuInfo = productFeignService.getSkuInfo(skuId);
                SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });

                // 2. 商品添加到购物车
                cartItem.setSkuId(skuId);
                cartItem.setCount(num);
                cartItem.setCheck(true);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setPrice(data.getPrice());
            }, executor);

            // 3. 远程查询商品的组合信息
            CompletableFuture<Void> getSkuSaleAttrValuesTask = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(values);
            }, executor);

            // 4. 等待所有异步任务完成
            CompletableFuture.allOf(getSkuInfoTask, getSkuSaleAttrValuesTask).get();

            // 5. 将购物车信息保存到redis
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        }

        // 商品已存在于购物车，增加数量
        else {
            CartItem cartItem = new CartItem();

            // 1. 将json还原成对象
            cartItem = JSON.parseObject(result, CartItem.class);

            // 2. 增加购物项数量
            cartItem.setCount(cartItem.getCount() + num);

            // 3. 更新redis数据
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(getCartKey());
        String json = (String) cartOps.get(skuId.toString());
        return JSON.parseObject(json, CartItem.class);
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        Cart cart = new Cart();

        // 游客标识
        String tempCartKey = CartConstant.CACHE_CART_PREFIX + userInfoTo.getUserKey();

        // 1. 用户已登录
        if (!ObjectUtils.isEmpty(userInfoTo.getUserId())) {
            String cartKey = CartConstant.CACHE_CART_PREFIX + userInfoTo.getUserId().toString();

            // 1.1 获取临时购物车
            List<CartItem> cartItems = getCartItems(tempCartKey);

            // 1.1 如果临时购物车的数据还没有合并，那么合并用户购物车和游客购物车
            if (!CollectionUtils.isEmpty(cartItems)) {
                log.info("用户[{}]的购物车已合并", userInfoTo.getUsername());
                for (CartItem item : cartItems) {
                    addToCart(item.getSkuId(), item.getCount());
                }
            }

            // 1.2 获取登录状态的购物车数据
            List<CartItem> result = getCartItems(cartKey);
            log.debug("用户[{}]的购物车详情：{}", userInfoTo.getUsername(),
                    !CollectionUtils.isEmpty(result) ? result : "空空如也"
            );
            cart.setItems(result);

            // 1.3 清空临时购物车
            clearCart(tempCartKey);
        }

        // 2. 用户未登录
        else {
            List<CartItem> result = getCartItems(tempCartKey);
            log.debug("游客[{}]的购物车详情：{}", userInfoTo.getUsername(),
                    !CollectionUtils.isEmpty(result) ? result : "空空如也"
            );
            cart.setItems(result);
        }

        return cart;
    }

    @Override
    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(getCartKey());
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check == 1);
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(getCartKey());
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(getCartKey());
        cartOps.delete(skuId.toString());
    }

    @Override
    public BigDecimal toTrade() throws ExecutionException, InterruptedException {
        BigDecimal amount = getCart().getTotalAmount();
        redisTemplate.delete(getCartKey());
        return amount;
    }

    @Override
    public List<CartItem> getUserCartItems() {
        String cartKey = getCartKey();
        if (cartKey.startsWith(CartConstant.CACHE_CART_PREFIX + CartConstant.CACHE_CART_VISITOR_PREFIX)) {
            return null;
        }
        List<CartItem> cartItems = getCartItems(cartKey);
        List<CartItem> checkItems = cartItems.stream().filter(CartItem::getCheck)
                .map(item -> {
                    // 查询最新价格
                    try {
                        R r = productFeignService.getPrice(item.getSkuId());
                        String price = (String) r.get("data");
                        item.setPrice(new BigDecimal(price));
                    } catch (Exception e) {
                        log.warn("远程查询商品价格失败：[商品服务可能未启动]");
                    }
                    return item;
                })
                .collect(Collectors.toList());
        return checkItems;
    }

    /**
     * 获取用户购物车的redis-key
     * <ul>
     * <li>登录: userId</li>
     * <li>游客：userKey</li>
     * </ul>
     * @return cartKey
     */
    private String getCartKey() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();

        return CartConstant.CACHE_CART_PREFIX + (userInfoTo.getUserId() != null ?
                userInfoTo.getUserId() : userInfoTo.getUserKey());
    }

    /**
     * 获取用户购物车的redis-hash操作集合
     * @param cartKey 用户购物车key
     * @return 获取要操作的购物车
     */
    private BoundHashOperations<String, Object, Object> getCartOps(String cartKey) {
        return redisTemplate.boundHashOps(cartKey);
    }

    /**
     * 获取购物车所有购物项
     * @param cartKey 用户购物车key
     * @return 购物项列表
     */
    private List<CartItem> getCartItems(String cartKey) {
        log.debug("获取购物项，cartKey: {}", cartKey);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps(cartKey);
        List<Object> values = cartOps.values();

        if (!CollectionUtils.isEmpty(values)) {
            return values.stream().map(str -> JSON.parseObject((String) str, CartItem.class))
                    .collect(Collectors.toList());
        }
        return null;
    }

}
