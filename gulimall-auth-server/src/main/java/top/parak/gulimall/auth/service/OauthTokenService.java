package top.parak.gulimall.auth.service;

import org.springframework.http.ResponseEntity;
import top.parak.gulimall.auth.oauth.AbstractOauthProperties;

/**
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 */
public interface OauthTokenService {

    /**
     * OAuth2.0 授权码模式获取令牌
     * @param properties oauth配置
     * @param code       授权码
     * @return 令牌
     */
    ResponseEntity<String> getAccessToken(AbstractOauthProperties properties, String code);

}
