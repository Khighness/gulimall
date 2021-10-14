package top.parak.gulimall.thirdparty;

import static org.junit.jupiter.api.Assertions.*;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTest {

    @Resource
    private OSSClient ossClient;

    @Test
    void aliOss() throws IOException {
        // 填写网络流地址。
        InputStream inputStream = new FileInputStream("C:\\Users\\18236\\Pictures\\K\\wu.jpg");// 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        ossClient.putObject("khighness-gulimall", "wu.jpg", inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

}
