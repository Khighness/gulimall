package top.parak.gulimall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 分布式Session配置
 *
 * @author KHighness
 * @since 2022-01-05
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

    /**
     * 自定义session序列化器
     * 名称必须是springSessionDefaultRedisSerializer
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

}
