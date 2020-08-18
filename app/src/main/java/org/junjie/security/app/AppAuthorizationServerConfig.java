package org.junjie.security.app;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.junjie.security.core.properties.OAuth2ClientProperties;
import org.junjie.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;

@Configuration
//声明开启 OAuth 授权服务器的功能。
@EnableAuthorizationServer
@Slf4j
public class AppAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    /**
     * 数据源 DataSource
     */
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Autowired
    private TokenStore redisTokenStore;

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


    /**
     * tokenKey的访问权限表达式配置
     */
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //CheckTokenEndpoint
//        security.tokenKeyAccess("permitAll()")
//                .checkTokenAccess("isAuthenticated()");
        security
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //使用jdbc,此方式新增加的client可以即时生效，inMemory默认不是即时生效
        clients.withClientDetails(jdbcClientDetailsService());
        //使用内存
//        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
//        if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
//            for (OAuth2ClientProperties config : securityProperties.getOauth2().getClients()) {
//                builder
//                        .withClient(config.getClientId())
//                        .secret(passwordEncoder.encode(config.getClientSecret()))//这里一定要这样用
//                        .accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())//令牌有效期
//                        .authorizedGrantTypes("refresh_token", "password")
//                        .refreshTokenValiditySeconds(2592000)//refreshToken的有效期
//                        .scopes("all");
//            }
//        }
    }
}
