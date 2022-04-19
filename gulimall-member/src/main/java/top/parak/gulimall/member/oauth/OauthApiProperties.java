package top.parak.gulimall.member.oauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 第三方Oauth的api配置
 *
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth.api")
public class OauthApiProperties {

    private String github;

    private String yuque;

    private String weibo;

}
