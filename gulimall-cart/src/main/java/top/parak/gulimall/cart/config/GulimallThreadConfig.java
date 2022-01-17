package top.parak.gulimall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author KHighness
 * @since 2021-01-03
 * @email parakovo@gmail.com
 */
@Configuration
// @EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class GulimallThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool) {
        return new ThreadPoolExecutor(
                pool.getCoreSize(),
                pool.getMaxSize(),
                pool.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10_0000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "gulimall.thread")
    public static class ThreadPoolConfigProperties {

        private Integer coreSize;

        private Integer maxSize;

        private Integer keepAliveTime;

    }

}
