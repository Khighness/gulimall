package top.parak.gulimall.auth.oauth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Github OAuth2.0 配置
 *
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 */
@Component
@ConfigurationProperties(prefix = "oauth.github")
@Data
@EqualsAndHashCode(callSuper = true)
public class GithubOauthProperties extends AbstractOauthProperties {

}
