package top.parak.gulimall.member.exception;

/**
 * 注册时手机号已存在则抛出
 *
 * @author KHighness
 * @since 2021-12-29
 * @email parakovo@gmail.com
 */
public class PhoneRegisteredException extends RuntimeException {

    public PhoneRegisteredException() {
        super("手机号已注册");
    }

}
