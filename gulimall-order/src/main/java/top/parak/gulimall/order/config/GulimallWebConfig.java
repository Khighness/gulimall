package top.parak.gulimall.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.parak.gulimall.order.interceptor.LoginUserInterceptor;

/**
 * 拦截器配置
 *
 * @author KHighness
 * @since 2022-01-09
 * @email parakovo@gmail.com
 */
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginUserInterceptor loginUserInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**");
    }

}
