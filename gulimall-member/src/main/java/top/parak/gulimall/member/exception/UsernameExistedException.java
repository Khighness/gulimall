package top.parak.gulimall.member.exception;

/**
 * @author KHighness
 * @since 2021-12-29
 * @email parakovo@gmail.com
 */
public class UsernameExistedException extends RuntimeException {

    public UsernameExistedException() {
        super("用户名已存在");
    }

}
