package top.parak.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author KHighness
 * @since 2021-12-12
 * @apiNote Redisson配置
 */
@Configuration
public class MyRedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.117.155:6379")
                .setPassword("KAG1823")
                .setDatabase(0);

        return Redisson.create(config);
    }

}
