package top.parak.gulimall.ssoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import top.parak.gulimall.ssoserver.entity.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2022-01-01
 * @email parakovo@gmail.com
 */
@Controller
public class LoginController {
    private static final String CACHE_TOKEN_PREFIX = "gulimall:sso:token:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @ResponseBody
    @GetMapping("/info")
    public String info(@RequestParam("token") String token) {
        return stringRedisTemplate.opsForValue().get(CACHE_TOKEN_PREFIX + token);
    }

    /**
     * SSO认证
     * @see <a href="http://ssoserver.com:15000/login.html?username=Khighness">login</a>
     */
    @GetMapping("/login.html")
    public String loginPage(User user, Model model, HttpSession session,
                            @CookieValue(value = "sso_token", required = false) String ssoToken) {
        // 令牌不为空
        if (!StringUtils.isEmpty(ssoToken)) {
            String username = stringRedisTemplate.opsForValue().get(CACHE_TOKEN_PREFIX + ssoToken);
            // 检验令牌是否过期
            if (!StringUtils.isEmpty(username)) {
                // 跳转到期望URL
                if (!StringUtils.isEmpty(user.getUrl())) {
                    return "redirect:" + user.getUrl() + "?username=" + user.getUsername() + "&token=" + ssoToken;
                }
                // 没有期望URL，跳转到默认页面
                else {
                    session.setAttribute("username", user.getUsername());
                    return "index";
                }
            }
        }
        // 令牌为空，返回登录
        model.addAttribute("url", user.getUrl());
        model.addAttribute("username", user.getUsername());
        return "login";
    }

    /**
     * 执行登录逻辑
     */
    @PostMapping("/doLogin")
    public String doLogin(User user, HttpServletResponse response) {
        // 校验用户名和密码
        if (!StringUtils.isEmpty(user.getUsername()) && !StringUtils.isEmpty(user.getPassword())
                && "Khighness".equals(user.getUsername()) && "Khighness".equals(user.getPassword())) {

            // 登录成功，设置cookie
            String ssoToken = UUID.randomUUID().toString().replace("=", "");
            Cookie cookie = new Cookie("sso_token", ssoToken);
            response.addCookie(cookie);

            // 将令牌信息存入redis
            stringRedisTemplate.opsForValue().set(CACHE_TOKEN_PREFIX + ssoToken, user.getUsername(),
                    30, TimeUnit.MINUTES);

            return "redirect:" + user.getUrl() + "?username=" + user.getUsername() + "&token=" + ssoToken;
        }
        // 登录失败
        return "login";
    }

}
