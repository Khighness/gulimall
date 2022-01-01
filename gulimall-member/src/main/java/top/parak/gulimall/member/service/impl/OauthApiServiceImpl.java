package top.parak.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import top.parak.gulimall.common.oauth.AbstractOauthToken;
import top.parak.gulimall.common.oauth.GithubOauthToken;
import top.parak.gulimall.common.oauth.WeiboOauthToken;
import top.parak.gulimall.common.oauth.YuqueOauthToken;
import top.parak.gulimall.member.oauth.GithubUser;
import top.parak.gulimall.member.oauth.OauthApiProperties;
import top.parak.gulimall.member.oauth.YuqueUser;
import top.parak.gulimall.member.service.OauthApiService;

/**
 * @author KHighness
 * @since 2021-12-30
 */
@Service
public class OauthApiServiceImpl implements OauthApiService {

    @Autowired
    private OauthApiProperties oauthApiProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GithubUser getGithubUserInfo(GithubOauthToken oauthToken) {
        checkToken(oauthToken);

        // 构造请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "token " + oauthToken.getAccessToken());

        // 构造请求体
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        // 发送GET请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(oauthApiProperties.getGithub(),
                HttpMethod.GET, httpEntity, String.class);

        // 判断请求状态
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return JSON.parseObject(responseEntity.getBody(), new TypeReference<GithubUser>() { });
        }

        // 请求失败，返回空
        return null;
    }

    @Override
    public YuqueUser getYuqueUserInfo(YuqueOauthToken oauthToken) {
        checkToken(oauthToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Auth-Token", oauthToken.getAccessToken());

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(oauthApiProperties.getYuque(),
                HttpMethod.GET, httpEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            JSONObject json = JSONObject.parseObject(responseEntity.getBody());
            String data = json.getString("data");
            return JSON.parseObject(data, new TypeReference<YuqueUser>() { });
        }

        return null;
    }

    @Override
    public WeiboOauthToken getWeiboUserInfo(WeiboOauthToken oauthToken) {
        checkToken(oauthToken);
        return null;
    }

    private void checkToken(AbstractOauthToken oauthToken) {
        if (ObjectUtils.isEmpty(oauthToken)) {
            throw new IllegalArgumentException("OAuth token can not be empty");
        }
    }

}
