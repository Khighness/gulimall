package top.parak.gulimall.auth.vo;

import lombok.Data;

/**
 * 用户登录信息
 *
 * @author KHighness
 * @since 2021-12-29
 * @email parakovo@gmail.com
 */
@Data
public class UserLoginVo {

    private String loginAccount;

    private String password;

}
