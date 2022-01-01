package top.parak.gulimall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author KHighness
 * @since 2021-12-31
 * @email parakovo@gmail.com
 */
@Configuration
public class GulimallSessionConfig {

    /**
     * spring-session自定义cookie序列化器
     *
     * @see <a href="https://docs.spring.io/spring-session/docs/2.2.1.RELEASE/reference/html5/#api-cookieserializer">api-cookieserializer</a>
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();

        cookieSerializer.setDomainName("gulimall.com");
        cookieSerializer.setCookieName("GULISESSION");

        return cookieSerializer;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

}
