统一认证登录认证中心 SSO

客户端（资源服务器）使用时需要添加使用如下配置，session名称一定要配置
client_id、client_secret为认证服务中心定义好的
user-authorization-uri、access-token-uri、token-info-uri根据实际
认证中心的地址配置，同时客户端使用@EnableOAuth2Sso 开启Sso功能

源码类
OAuth2ClientContextFilter
OAuth2ClientAuthenticationProcessingFilter
```yaml
server:
  port: 9090
  servlet:
    session:
      cookie:
        name: SSO-SESSIONID # 自定义 Session 的 Cookie 名字，防止冲突。冲突后，会导致 SSO 登录失败
security:
  oauth2:
    # OAuth2 Client 配置，对应 OAuth2ClientProperties 类
    client:
      client-id: clientapp
      client-secret: 112233
      user-authorization-uri: http://127.0.0.1:8080/oauth/authorize # 获取用户的授权码地址
      access-token-uri: http://127.0.0.1:8080/oauth/token # 获取访问令牌的地址
    # OAuth2 Resource 配置，对应 ResourceServerProperties 类
    resource:
      token-info-uri: http://127.0.0.1:8080/oauth/check_token # 校验访问令牌是否有效的地址
```
