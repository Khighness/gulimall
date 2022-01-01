package top.parak.gulimall.ssoclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author KHighness
 * @since 2022-01-01
 * @email parakovo@gmail.com
 */
@Component
@ConfigurationProperties(prefix = "sso.server")
@Data
public class SsoServerProperties {

    private String host;

    private String path;

}
