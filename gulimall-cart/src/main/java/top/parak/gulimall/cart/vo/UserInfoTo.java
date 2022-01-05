package top.parak.gulimall.cart.vo;

import lombok.Data;

/**
 * @author KHighness
 * @since 2022-01-03
 * @email parakovo@gmail.com
 */
@Data
public class UserInfoTo {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 临时用户
     */
    private String userKey;

    /**
     * 是否是临时用户
     */
    private boolean tempUser = false;

}
