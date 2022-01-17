package top.parak.gulimall.common.exception;

/**
 * 业务状态码
 * <ul>
 * <li>10XXX: 通用</li>
 * <li>11XXX: 商品</li>
 * <li>12XXX: 订单</li>
 * <li>13XXX: 购物车</li>
 * <li>14XXX: 物流</li>
 * <li>15XXX: 用户</li>
 * <li>21XXX: 库存</li>
 * </ul>
 *
 * @author KHighness
 * @since 2021-10-13
 * @email parakovo@gmail.com
 */
public enum BizCodeEnum {

    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    SMS_CODE_EXCEPTION(10002, "短信验证码请求频繁，稍后再试"),
    TOO_MANY_EXCEPTION(10003, "您点的太快了，休息一下吧"),
    SMS_COD_SEND_EXCEPTION(10004, "短信发送失败"),
    USER_EXISTED_EXCEPTION(15001, "用户名已存在"),
    PHONE_REGISTERED_EXCEPTION(15002, "手机号已注册"),
    LOGIN_ACCOUNT_OR_PASSWORD_EXCEPTION(15003, "账号或者密码错误"),
    SOCIAL_USER_LOGIN_EXCEPTION(15004, "社交账号登录失败"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),
    NO_STOCK_EXCEPTION(21000, "商品库存不足");


    private Integer code;
    private String message;

    BizCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
