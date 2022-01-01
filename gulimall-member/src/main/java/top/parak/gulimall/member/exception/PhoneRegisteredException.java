package top.parak.gulimall.member.exception;

/**
 * @author KHighness
 * @since 2021-12-29
 * @email parakovo@gmail.com
 */
public class PhoneRegisteredException extends RuntimeException {

    public PhoneRegisteredException() {
        super("手机号已注册");
    }

}
