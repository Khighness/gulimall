package top.parak.gulimall.thirdparty.component;

import com.alibaba.alicloud.context.AliCloudProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author KHighness
 * @since 2021-12-31
 * @email parakovo@gmail.com
 */
@Component
@ConfigurationProperties(prefix = "spring.cloud.alicloud.oss")
@Data
@Slf4j
public class OssComponent {

    @Component
    private static class OssConfigRunner implements ApplicationRunner {

        @Autowired
        private OssComponent ossComponent;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            log.info("AliCloud OSS [bucket: {}, endpoint: {}]",
                    ossComponent.getBucket(), ossComponent.getEndpoint()
            );
        }
    }

    private String bucket;

    private String endpoint;

}
