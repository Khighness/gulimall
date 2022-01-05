package top.parak.gulimall.auth;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 认证服务启动器
 *
 * @author KHighness
 * @since 2021-12-26
 * @email parakovo@gmail.com
 * @apiNote spring session principle
 * <br>
 * 1. EnableRedisHttpSession给容器中导入了RedisHttpSessionConfiguration          「Redis存储Session自动排名配置类」<br>
 * 2. RedisHttpSessionConfiguration给容器中注册了RedisOperationSessionRepository「封装Redis对Session的增删改查操作」<br>
 * 3. RedisOperationSessionRepository继承了SpringHttpSessionConfiguration     「Spring原生的Session自动配置」<br>
 * 4. SpringHttpSessionConfiguration给容器中注册了SessionRepositoryFilter      「Servlet过滤器，会处理所有请求」<br>
 * 5. 🚀 SessionRepositoryFilter的doFilterInternal()方法 <br>
 * 5.1 在请求中通过{@code setAttribute() }设置了一个RedisOperationSessionRepository，在整个请求期间会被共享 <br>
 * 5.2 将原生的请求和响应使用装饰者模式进行包装 「request, response」 -> 「wrappedRequest, wrappedResponse」<br>
 * 5.3 wrappedRequest重写了{@code getSession() }方法，内部实现了自定义业务逻辑，从redis中获取，空则移除session，有则设置 <br>
 *
 * @see org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
 * @see org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration
 * @see org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration
 * @see org.springframework.session.web.http.SessionRepositoryFilter#doFilterInternal
 * @see org.springframework.session.web.http.SessionRepositoryFilter.SessionRepositoryRequestWrapper
 * @see org.springframework.session.web.http.SessionRepositoryFilter.SessionRepositoryResponseWrapper
 * @see org.springframework.session.web.http.SessionRepositoryFilter.SessionRepositoryRequestWrapper#getSession
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "top.parak.gulimall.auth.feign")
@EnableRedisHttpSession
@SpringBootApplication(
        exclude =  { DataSourceAutoConfiguration.class }
)
public class GulimallAuthServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(GulimallAuthServerApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
