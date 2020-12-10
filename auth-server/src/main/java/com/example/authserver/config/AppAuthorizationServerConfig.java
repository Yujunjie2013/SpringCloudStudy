package com.example.authserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;


//声明开启 OAuth 授权服务器的功能。
@EnableAuthorizationServer
@Slf4j
@Import(TokenGranterConfig.class)
@Configuration
public class AppAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    /**
     * 数据源 DataSource
     */
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService myUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Autowired
    private TokenStore redisTokenStore;
    @Autowired
    private AuthorizationCodeServices randomValueAuthorizationCodeServices;

    @Autowired
    private TokenGranter tokenGranter;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        if (redisTokenStore == null) {
            log.error("token是空");
        }
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(redisTokenStore)
                .userDetailsService(myUserDetailsService)
                .authorizationCodeServices(randomValueAuthorizationCodeServices)
                .tokenGranter(tokenGranter);
        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            ArrayList<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            tokenEnhancerChain.setTokenEnhancers(enhancers);
            endpoints.tokenEnhancer(tokenEnhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }


    /**
     * tokenKey的访问权限表达式配置
     */
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //CheckTokenEndpoint
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //1、使用jdbc,此方式新增加的client可以即时生效，
        clients.withClientDetails(jdbcClientDetailsService());
        //2、使用内存的方式、新增的client不会即时生效
//        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
//        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
//            for (OAuth2ClientProperties config : securityProperties.getOauth2().getClients()) {
//                builder
//                        .withClient(config.getClientId())
//                        .secret(passwordEncoder.encode(config.getClientSecret()))//这里一定要这样用
//                        .accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())//令牌有效期
//                        .authorizedGrantTypes("refresh_token", "authorization_code", "password")
//                        .refreshTokenValiditySeconds(2592000)//refreshToken的有效期
//                        .scopes("all", "read_userinfo", "read_contacts")
//                        .redirectUris("http://127.0.0.1:9001/login", "http://127.0.0.1:9090/callback");
//            }
//        }
    }
}
