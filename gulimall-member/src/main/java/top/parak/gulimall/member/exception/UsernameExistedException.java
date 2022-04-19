package top.parak.gulimall.member.exception;

/**
 * 注册时用户名已存在则抛出
 *
 * @author KHighness
 * @since 2021-12-29
 * @email parakovo@gmail.com
 */
public class UsernameExistedException extends RuntimeException {

    public UsernameExistedException() {
        super("用户名已存在");
    }

}
