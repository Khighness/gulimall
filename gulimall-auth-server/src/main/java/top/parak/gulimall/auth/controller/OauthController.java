package top.parak.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import top.parak.gulimall.auth.feign.MemberFeignService;
import top.parak.gulimall.auth.oauth.WeiboOauthProperties;
import top.parak.gulimall.auth.service.OauthTokenService;
import top.parak.gulimall.auth.oauth.GithubOauthProperties;
import top.parak.gulimall.common.cosntant.AuthServerConstant;
import top.parak.gulimall.common.oauth.AbstractOauthToken;
import top.parak.gulimall.common.oauth.GithubOauthToken;
import top.parak.gulimall.auth.oauth.YuqueOauthProperties;
import top.parak.gulimall.common.oauth.WeiboOauthToken;
import top.parak.gulimall.common.oauth.YuqueOauthToken;
import top.parak.gulimall.common.cosntant.GulimallPageConstant;
import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.common.vo.MemberResponseVo;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方认证
 * <p><p><b>步骤</b>
 * <ol>
 * <li>请求用户的第三方应用身份 </li>
 * <li>用户被第三方应用重定向回Gulimall </li>
 * <li>使用访问令牌访问第三方应用API </li>
 * </ol>
 *
 * @author KHighness
 * @since 2021-12-26
 * @email parakovo@gmail.com
 */
@Slf4j
@Controller
public class OauthController {

    @Autowired
    private MemberFeignService memberFeignService;

    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private GithubOauthProperties githubOauthProperties;

    @Autowired
    private YuqueOauthProperties yuqueOauthProperties;

    @Autowired
    private WeiboOauthProperties weiboOauthProperties;

    /**
     * Github认证
     *
     * <ol>
     * <li>登录页面 GET https://github.com/login/oauth/authorize</li>
     * <li>认证中心 POST https://github.com/login/oauth/access_token</li>
     * <li>会员服务 curl -H "Authorization: token ${token}" https://api.github.com/user</li>
     * </ol>
     *
     * @see <a href="https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps">Github OAuth Doc</a>
     */
    @GetMapping("/oauth2.0/github/success")
    public Object githubAuthorize(@RequestParam("code") String code, HttpSession session) {
        // 1. 使用授权码换取令牌
        ResponseEntity<String> responseEntity = oauthTokenService.getAccessToken(githubOauthProperties, code);
        Map<String, String> errors = new HashMap<>();

        // 2. 成功则取出令牌，调用会员服务进行登录
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            GithubOauthToken gitHubOauthToken = JSON.parseObject(responseEntity.getBody(),
                    new TypeReference<GithubOauthToken>() { });

            String loginResult = handleSocialLogin(session, errors, gitHubOauthToken, code,
                    AuthServerConstant.SOCIAL_PLATFORM_GITHUB);

            if (!ObjectUtils.isEmpty(loginResult)) {
                return loginResult;
            }
        }

        // 3. 失败，返回登录页面，提示错误信息
        return handleAuthorizationError(session, errors, AuthServerConstant.SOCIAL_PLATFORM_GITHUB, code);
    }

    /**
     * 语雀认证
     *
     * <ol>
     * <li>登录页面 GET https://www.yuque.com/oauth2/authorize</li>
     * <li>认证中心 POST https://www.yuque.com/oauth2/token</li>
     * <li>会员服务 curl -H "X-Auth-Token: ${token}" https://www.yuque.com/api/v2/user</li>
     * </ol>
     *
     * @see <a href="https://www.yuque.com/yuque/developer/authorizing-oauth-apps">Yuque Oauth Doc</a>
     */
    @GetMapping("/oauth2.0/yuque/success")
    public Object yuqueAuthorize(@RequestParam("code") String code, HttpSession session) throws Exception {
        ResponseEntity<String> responseEntity = oauthTokenService.getAccessToken(yuqueOauthProperties, code);
        Map<String, String> errors = new HashMap<>();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            YuqueOauthToken yuqueOauthToken = JSON.parseObject(responseEntity.getBody(),
                    new TypeReference<YuqueOauthToken>() { });

            String loginResult = handleSocialLogin(session, errors, yuqueOauthToken, code,
                    AuthServerConstant.SOCIAL_PLATFORM_YUQUE);

            if (!ObjectUtils.isEmpty(loginResult)) {
                return loginResult;
            }
        }

        return handleAuthorizationError(session, errors, AuthServerConstant.SOCIAL_PLATFORM_YUQUE, code);
    }

    /**
     * 微博认证
     *
     * <ol>
     * <li>登录页面 GET https://api.weibo.com/oauth2/authorize</li>
     * <li>认证中心 POST https://api.weibo.com/oauth2/access_token</li>
     * <li>会员服务 curl https://api.weibo.com/2/users/show.json?access_token=${access_token}&uid=${uid}</li>
     * </ol>
     *
     * @see <a href="https://open.weibo.com/wiki/%E6%8E%88%E6%9D%83%E6%9C%BA%E5%88%B6">Weibo Oauth Wiki</a>
     */
    @GetMapping("/oauth2.0/weibo/success")
    public Object weiboAuthorize(@RequestParam("code") String code, HttpSession session) throws Exception {
        ResponseEntity<String> responseEntity = oauthTokenService.getAccessToken(weiboOauthProperties, code);
        Map<String, String> errors = new HashMap<>();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            WeiboOauthToken weiboOauthToken = JSON.parseObject(responseEntity.getBody(),
                    new TypeReference<WeiboOauthToken>() { });

            log.info("{}", weiboOauthToken);

            String loginResult = handleSocialLogin(session, errors, weiboOauthToken, code,
                    AuthServerConstant.SOCIAL_PLATFORM_WEIBO);

            if (!ObjectUtils.isEmpty(loginResult)) {
                return loginResult;
            }
        }

        return handleAuthorizationError(session, errors, AuthServerConstant.SOCIAL_PLATFORM_WEIBO, code);
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String login(HttpSession session) {
        MemberResponseVo member = (MemberResponseVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (!ObjectUtils.isEmpty(member)) {
            log.info("用户下线：[{}]", member.getUsername());
        }

        session.invalidate();
        return GulimallPageConstant.REDIRECT + GulimallPageConstant.LOGIN_PAGE;
    }

    /**
     * 使用令牌进行第三方登录
     */
    private String handleSocialLogin(HttpSession session, Map<String, String> errors, AbstractOauthToken oauthToken,
                                     String code, String socialPlatform) {
        if (!ObjectUtils.isEmpty(oauthToken)) {
            log.info("{} OAuth 授权成功，code：[{}]，access_token：[{}]",
                    socialPlatform, code, oauthToken.getAccessToken());

            R r = null;
            if (oauthToken instanceof GithubOauthToken) {
                r = memberFeignService.login((GithubOauthToken) oauthToken);
            } else if (oauthToken instanceof YuqueOauthToken) {
                r = memberFeignService.login((YuqueOauthToken) oauthToken);
            } else if (oauthToken instanceof WeiboOauthToken) {
                r = memberFeignService.login((WeiboOauthToken) oauthToken);
            }

            if (!ObjectUtils.isEmpty(r) && r.getCode() == 0) {
                String json = JSONObject.toJSONString(r.get("memberEntity"));
                MemberResponseVo memberResponseVo = JSON.parseObject(json,
                        new TypeReference<MemberResponseVo>() { });

                log.info("用户[用户名：{}，社交账号ID：{}] 社交登录成功",
                        memberResponseVo.getUsername(), memberResponseVo.getSocialUid());

                // redis存储分布式session
                //  1. 作用域: .gulimall.com
                //  2. 序列化： JSON-serialization

                session.setAttribute(AuthServerConstant.LOGIN_USER, memberResponseVo);
                return GulimallPageConstant.REDIRECT + GulimallPageConstant.INDEX_PAGE;
            } else {
                log.info("{} OAuth 登录失败，code：[{}]", socialPlatform, code);

                errors.put("msg", socialPlatform + "登录失败，请重试");
                session.setAttribute("errors", errors);
                return GulimallPageConstant.REDIRECT + GulimallPageConstant.LOGIN_PAGE;
            }
        }

        // 授权失败返回空
        return null;
    }

    /**
     * 处理第三方授权失败
     */
    private String handleAuthorizationError(HttpSession session, Map<String, String> errors,
                                    String socialPlatform, String code) {
        log.warn("{} OAuth 授权失败，code：[{}]", socialPlatform, code);

        errors.put("msg", socialPlatform + "授权失败，请重试");
        session.setAttribute("errors", errors);
        return GulimallPageConstant.REDIRECT + GulimallPageConstant.LOGIN_PAGE;
    }

}
