package top.parak.gulimall.common.cosntant;

/**
 * @author KHighness
 * @since 2021-12-28
 * @email parakovo@gmail.com
 */
public class AuthServerConstant {

    /**
     * Redis存储：手机验证码前缀
     */
    public static final String SMS_CODE_CACHE_PREFIX = "gulumall:sms:code:";

    /**
     * Session存储：登录用户信息
     */
    public static final String LOGIN_USER = "loginUser";

    /**
     * 未登录提升信息
     */
    public static final String NOT_LOGIN_MESSAGE = "请先进行登录";

    /**
     * 社交平台：Github
     */
    public static final String SOCIAL_PLATFORM_GITHUB = "Github";
    /**
     * 社交平台：语雀
     */
    public static final String SOCIAL_PLATFORM_YUQUE = "语雀";
    /**
     * 社交平台：微博
     */
    public static final String SOCIAL_PLATFORM_WEIBO = "微博";

}
