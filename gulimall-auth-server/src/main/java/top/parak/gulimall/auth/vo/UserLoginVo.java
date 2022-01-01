package top.parak.gulimall.auth.vo;

import lombok.Data;

/**
 * @author KHighness
 * @since 2021-12-29
 * @apiNote 用户登录信息
 * @email parakovo@gmail.com
 */
@Data
public class UserLoginVo {

    private String loginAccount;

    private String password;

}
