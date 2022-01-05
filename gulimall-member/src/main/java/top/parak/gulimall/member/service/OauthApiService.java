package top.parak.gulimall.member.service;

import top.parak.gulimall.common.oauth.GithubOauthToken;
import top.parak.gulimall.common.oauth.WeiboOauthToken;
import top.parak.gulimall.common.oauth.YuqueOauthToken;
import top.parak.gulimall.member.oauth.GithubUser;
import top.parak.gulimall.member.oauth.WeiboUser;
import top.parak.gulimall.member.oauth.YuqueUser;

/**
 * @author KHighness
 * @since 2021-12-30
 */
public interface OauthApiService {

    GithubUser getGithubUserInfo(GithubOauthToken oauthToken);

    YuqueUser getYuqueUserInfo(YuqueOauthToken oauthToken);

    WeiboUser getWeiboUserInfo(WeiboOauthToken oauthToken);

}
