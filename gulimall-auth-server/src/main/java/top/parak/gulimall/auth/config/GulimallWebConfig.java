package top.parak.gulimall.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author KHighness
 * @since 2021-12-28
 * @email parakovo@gmail.com
 */
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {

    /**
     * 视图映射
     * <ul>
     * <li>注册</li>
     * </ul>
     *
     * @param registry 映射器注册中心
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
