package top.parak.gulimall.thirdparty.component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.parak.gulimall.common.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KHighness
 * @since 2021-12-28
 * @email parakovo@gmail.com
 */
@Component
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Data
@Slf4j
public class SmsComponent {

    @Component
    private static class SmsConfigRunner implements ApplicationRunner {

        @Autowired
        private SmsComponent smsComponent;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            log.info("AliCloud SMS [host: {}, path: {}, callbackUrl: {}, templateId: {}]",
                    smsComponent.getHost(), smsComponent.getPath(),
                    smsComponent.getCallbackUrl(), smsComponent.getTemplateId()
            );
        }
    }

    private String appCode;

    private String host;

    private String path;

    private String callbackUrl;

    private String templateId;

    /**
     * Body
     * <pre>
     * +------------------+---------+-----------+--------------------+
     * |      Name        |   Type  |  Require  |     Description    |
     * +------------------+---------+-----------+--------------------+
     * |    callbackUrl   |  String |   false   |   callback url     |
     * +------------------+---------+-----------+--------------------+
     * |     mobileSet    |  String |   true    |     mobile set     |
     * +------------------+---------+-----------+--------------------+
     * |    templateId    |  String |   true    |    template id     |
     * +------------------+---------+-----------+--------------------+
     * | templateParamSet |  String |   false   | separated with ',' |
     * +------------------+---------+-----------+--------------------+
     *
     * @see <a href="https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java">HttpUtils.java</a>
     * @see <a href="https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml">pom.xml</a>
     * </pre>
     */
    public void sendSmsCode(String phone, String code) {
        String method = "POST";

        Map<String, String> headers = new HashMap<>();
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();

        // header: "Authorization:APPCODE ${appCode}"
        headers.put("Authorization", "APPCODE " + appCode);
        // 根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        // 设置请求体
        bodys.put("callbackUrl", callbackUrl);
        bodys.put("mobileSet", '[' + phone + ']');
        bodys.put("templateID", templateId);
        bodys.put("templateParamSet", code + ",1");

        // 发送请求
        try {
            log.info("发送手机验证码, 手机号: [{}], 验证码: [{}]", phone, code);
            // 防止阿里云短信次数刷完，测试仅输出在控制台
//            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            log.info("发送手机验证码, 手机号: [{}], 验证码: [{}], 结果: [{}]", phone, code,
//                    EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            log.error("发送验证码失败, 手机号: [{}], 验证码: [{}]", phone, code, e);
        }
    }

}
