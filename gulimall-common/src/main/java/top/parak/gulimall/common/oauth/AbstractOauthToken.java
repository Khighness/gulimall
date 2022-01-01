package top.parak.gulimall.common.oauth;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 * @apiNote OAuth令牌抽象类
 */
@Data
public abstract class AbstractOauthToken {

    private String accessToken;

    private String tokenType;

    private String scope;

}
