package top.parak.gulimall.cart.service;

import top.parak.gulimall.cart.vo.Cart;
import top.parak.gulimall.cart.vo.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author KHighness
 * @since 2022-01-03
 * @email parakovo@gmail.com
 */
public interface CartService {

    /**
     * 添加商品到购物车
     * @param skuId 商品ID
     * @param num   购买数量
     * @return 购物项信息
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 根据SKU ID获取购物车中的购物项
     * @param skuId SKU ID
     * @return 购物项
     */
    CartItem getCartItem(Long skuId);

    /**
     * 获取购物车
     * @return 购物车
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车
     * @param cartKey 用户购物车key
     */
    void clearCart(String cartKey);

    /**
     * 勾选购物项
     * @param skuId SKU ID
     * @param check 勾选状态：1代表被选中，0代表未被选中
     */
    void checkItem(Long skuId, Integer check);

    /**
     * 修购购物数量
     * @param skuId SKU ID
     * @param num   购物数量
     */
    void changeItemCount(Long skuId, Integer num);

    /**
     * 删除购物项
     * @param skuId SKU ID
     */
    void deleteItem(Long skuId);

    /**
     * 结账
     * @return 总金额
     */
    BigDecimal toTrade() throws ExecutionException, InterruptedException;

    /**
     * 获取用户选中的购物项
     * @return 选中购物项列表
     */
    List<CartItem> getUserCartItems();

}
