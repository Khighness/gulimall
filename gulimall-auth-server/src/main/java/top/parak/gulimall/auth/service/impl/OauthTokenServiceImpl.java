package top.parak.gulimall.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.parak.gulimall.auth.oauth.AbstractOauthProperties;
import top.parak.gulimall.auth.service.OauthTokenService;

/**
 * @author KHighness
 * @since 2021-12-30
 * @email parakovo@gmail.com
 */
@Service
public class OauthTokenServiceImpl implements OauthTokenService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> getAccessToken(AbstractOauthProperties properties, String code) {
        // 构建请求URL
        StringBuilder accessTokenUrl = new StringBuilder(properties.getAccessHost())
                .append(properties.getAccessPath()).append('?')
                .append("client_id=").append(properties.getClientId())
                .append("&client_secret=").append(properties.getClientSecret())
                .append("&grant_type=").append(properties.getGrantType())
                .append("&code=").append(code);

        // 构建请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("accept", "application/json");

        // 构建请求实体
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        // 发送POST请求
        return restTemplate.exchange(accessTokenUrl.toString(), HttpMethod.POST, httpEntity, String.class);
    }

}
