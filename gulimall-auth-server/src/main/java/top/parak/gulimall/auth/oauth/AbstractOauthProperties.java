package top.parak.gulimall.auth.oauth;

import lombok.Data;

/**
 * OAuth配置抽象类
 *
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 */
@Data
public abstract class AbstractOauthProperties  {

    private String clientId;

    private String clientSecret;

    private String authorizeUrl;

    private String redirectUrl;

    private String accessHost;

    private String accessPath;

    private String grantType;

}
