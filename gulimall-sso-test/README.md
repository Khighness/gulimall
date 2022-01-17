## gulimall-sso-test

单点登录测试



### 项目结构

```
gulimall-sso-test
    ├───gulimall-sso-serever-test   🖥️ SSO服务器
    └───gulimall-sso-client-test    💻 音乐客户端
```



### HOSTS配置

windows系统，修改配置文件`C:\Windows\System32\drivers\etc\hosts`，添加如下配置：
```
##### SSO #####
127.0.0.1   ssoserver.com
127.0.0.1   client1.com
127.0.0.1   client2.com
```



### 场景说明

1. 用户访问音乐客户端的歌单。

2. 如果URL已经携带token，则去SSO服务器查询用户信息，用户信息不为空则访问歌单成功。
   
2. 其他情况下，都需要重定向回SSO服务器进行认证。

    1. SSO服务器先从cookie取出token。
    2. 如果不为空则检验token是否过期。
    3. 如果为空则重定向到登录页面。
    4. 用户登录成功则将token设置在cookie中。



### 访问测试

运行`GulimallSsoServerTestApplication`和`GulimallSsoClientTestApplication`

访问：http://client1.com:15001/song?username=Khighness

重定向到登录页面后，用户名和密码都是`Khighness`。
