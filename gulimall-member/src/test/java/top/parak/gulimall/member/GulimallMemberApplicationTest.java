package top.parak.gulimall.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.parak.gulimall.common.oauth.GithubOauthToken;
import top.parak.gulimall.common.oauth.YuqueOauthToken;
import top.parak.gulimall.member.oauth.GithubUser;
import top.parak.gulimall.member.oauth.YuqueUser;
import top.parak.gulimall.member.service.OauthApiService;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class GulimallMemberApplicationTest {

    @Autowired
    private OauthApiService oauthApiService;


    @Test
    void testYuque() {
        YuqueOauthToken yuqueOauthToken = new YuqueOauthToken();
        yuqueOauthToken.setAccessToken("Xy8rvNqdsfcnavoFLi8UksTcDqP7Whfx7iTgdHtj");
        YuqueUser userInfo = oauthApiService.getYuqueUserInfo(yuqueOauthToken);
        log.info("语雀用户: {}", userInfo);
    }

    @Test
    void testGithub() {
        GithubOauthToken githubOauthToken = new GithubOauthToken();
        githubOauthToken.setAccessToken("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");
        GithubUser userInfo = oauthApiService.getGithubUserInfo(githubOauthToken);
        log.info("Github用户: {}", userInfo);
    }

}
