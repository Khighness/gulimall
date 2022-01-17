package top.parak.gulimall.common.oauth;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OAuth令牌抽象类
 *
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 */
@Data
public abstract class AbstractOauthToken {

    private String accessToken;

    private String tokenType;

    private String scope;

}
