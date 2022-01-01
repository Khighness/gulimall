package top.parak.gulimall.ssoclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author KHighness
 * @since 2022-01-01
 * @email parakovo@gmail.com
 */
@Configuration
public class SsoRegistryConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
