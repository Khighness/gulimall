package top.parak.gulimall.ssoclient.cotroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import top.parak.gulimall.ssoclient.config.SsoServerProperties;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * @author KHighness
 * @since 2022-01-01
 * @email parakovo@gmail.com
 */
@Slf4j
@Controller
public class SongController {

    @Autowired
    private SsoServerProperties ssoServerProperties;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * hello，无需登录
     * @see <a href="http://client1.com:15001/hello?username=Khighness">hello</a>
     */
    @ResponseBody
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "username", required = true) String username) {
        return "Hello " + username;
    }

    /**
     * 查看歌单，需要登录
     * @see <a href="http://client1.com:15001/song?username=Khighness">song</a>
     */
    @GetMapping("/song")
    public String songPage(@RequestParam(value = "username", required = true) String username,
                          @RequestParam(value = "token", required = false) String token,
                          HttpSession session) {
        // 令牌为空，进行SSO登录
        if (StringUtils.isEmpty(token)) {
            return "redirect:" + ssoServerProperties.getHost() + ssoServerProperties.getPath()
                    + "?username=" + username + "&url=http://client1.com:15001/song";
        }
        // 令牌不为空，获取用户信息
        else {
            String url = ssoServerProperties.getHost() + "/info?" + "token=" + token;

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()), String.class);

            log.info("【用户访问】 {}", responseEntity.getBody());
        }
        List<String> albums = Arrays.asList(
                "Jay", "范特西", "八度空间", "叶惠美", "七里香",
                "十一月的肖邦", "依然范特西", "我很忙", "魔杰座",
                "跨时代", "惊叹号", "十二新作", "哎哟，不错哦",
                "周杰伦的床边故事"
        );
        session.setAttribute("username", username);
        session.setAttribute("albums", albums);
        return "song";
    }

}
