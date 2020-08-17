package org.junjie.security.app;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.junjie.security.core.properties.OAuth2ClientProperties;
import org.junjie.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;

@Configuration
@EnableAuthorizationServer
@Slf4j
public class AppAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private TokenStore redisTokenStore;
    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        if (redisTokenStore == null) {
            log.info("token是空");
        }
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(redisTokenStore)
                .userDetailsService(userDetailsService);
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

//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.passwordEncoder(passwordEncoder)
//                .tokenKeyAccess("isAuthenticated()");// 获取密钥需要身份认证
//
//    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
            for (OAuth2ClientProperties config : securityProperties.getOauth2().getClients()) {
                builder
                        .withClient(config.getClientId())
                        .secret(passwordEncoder.encode(config.getClientSecret()))//这里一定要这要用
                        .accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())//令牌有效期
                        .authorizedGrantTypes("refresh_token", "password")
                        .refreshTokenValiditySeconds(2592000)//refreshToken的有效期
                        .scopes("all");
            }
        }

    }
}
