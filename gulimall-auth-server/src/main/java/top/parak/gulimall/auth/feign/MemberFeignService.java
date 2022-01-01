package top.parak.gulimall.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.parak.gulimall.auth.vo.UserLoginVo;
import top.parak.gulimall.auth.vo.UserRegisterVo;
import top.parak.gulimall.common.oauth.GithubOauthToken;
import top.parak.gulimall.common.oauth.WeiboOauthToken;
import top.parak.gulimall.common.oauth.YuqueOauthToken;
import top.parak.gulimall.common.utils.R;

/**
 * @author KHighness
 * @since 2021-12-29
 * @email parakovo@gmail.com
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegisterVo registerVo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo loginVo);

    @PostMapping("/member/member/oauth2.0/github/login")
    R login(@RequestBody GithubOauthToken oauthToken);

    @PostMapping("/member/member/oauth2.0/yuque/login")
    R login(@RequestBody YuqueOauthToken oauthToken);

    @PostMapping("/member/member/oauth2.0/weibo/login")
    R login(@RequestBody WeiboOauthToken oauthToken);

}
