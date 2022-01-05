package top.parak.gulimall.common.cosntant;

/**
 * @author KHighness
 * @since 2022-01-03
 * @email parakovo@gmail.com
 */
public class CartConstant {

    /**
     * 游客状态用户标识key
     */
    public static final String VISITOR_COOKIE_NAME = "user-key";

    /**
     * 游客状态cookie过期时间
     */
    public static final int VISITOR_COOKIE_TIMEOUT = 259200;

    /**
     * 购物车redis存储分组
     */
    public static final String CACHE_CART_PREFIX = "gulimall:cart:";

    /**
     * 游客购物车存储前缀
     */
    public static final String CACHE_CART_VISITOR_PREFIX = "PARAK-";

}
