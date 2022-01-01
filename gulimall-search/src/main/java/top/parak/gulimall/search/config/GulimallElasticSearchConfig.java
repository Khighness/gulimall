package top.parak.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author KHighness
 * @since 2021-11-02
 * @email parakovo@gmail.com
 */
@Configuration
public class GulimallElasticSearchConfig {

    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient(GulimallElasticSearchProperties properties) {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(properties.getHost(), properties.getPort(), properties.getSchema())
        );
        return new RestHighLevelClient(builder);
    }

}
