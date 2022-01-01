package top.parak.gulimall.common.oauth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author KHighness
 * @since 2021-12-29
 * @email parakovo@gmail.com
 * @apiNote 微博令牌
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class WeiboOauthToken extends AbstractOauthToken {

    private String remindIn;

    /**
     * 令牌过期时间
     */
    private int expiresIn;

    /**
     * 用户唯一标识
     */
    private String uid;

    private String isRealName;

}
