package top.parak.gulimall.member.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign配置
 *
 * @author KHighness
 * @since 2022-01-19
 * @email parakovo@gmail.com
 */
@Configuration
public class GulimallFeignConfig {

    /**
     * 添加Feign拦截器，让Feign在远程调用时带上原客户端Cookie
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();
            if (!ObjectUtils.isEmpty(attributes)) {
                HttpServletRequest request = attributes.getRequest();
                if (!ObjectUtils.isEmpty(request)) {
                    String cookie = request.getHeader("Cookie");
                    requestTemplate.header("Cookie", cookie);
                }
            }
        };
    }

}
