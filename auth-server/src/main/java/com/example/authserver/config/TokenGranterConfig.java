package com.example.authserver.config;

import com.example.authserver.granter.MobilePwdGranter;
import com.example.authserver.granter.OpenIdGranter;
import com.example.authserver.granter.PwdImgCodeGranter;
import org.junjie.security.core.validate.code.ValidateCodeProcessorHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * token授权模式配置类，可添加自定义授权模式
 */
public class TokenGranterConfig {
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private TokenStore customRedisTokenStore;
    @Autowired(required = false)
    private List<TokenEnhancer> tokenEnhancer;
    @Autowired
    private UserDetailsService myUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    //系统中的校验码处理器
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;
    private TokenGranter tokenGranter;
    private AuthorizationServerTokenServices tokenServices;
    private boolean reuseRefreshToken = true;

    @Bean
    public AuthorizationCodeServices randomValueAuthorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    @Bean
    public TokenGranter tokenGranter(AuthorizationCodeServices authorizationCodeServices) {
        if (tokenGranter == null) {
            tokenGranter = new TokenGranter() {
                private CompositeTokenGranter delegate;

                @Override
                public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
                    if (delegate == null) {
                        delegate = new CompositeTokenGranter(Objects.requireNonNull(getAllTokenGranters(authorizationCodeServices)));
                    }
                    return delegate.grant(grantType, tokenRequest);
                }
            };
        }
        return tokenGranter;
    }

    private List<TokenGranter> getAllTokenGranters(AuthorizationCodeServices authorizationCodeServices) {
        AuthorizationServerTokenServices tokenServices = tokenServices();
        OAuth2RequestFactory requestFactory = requestFactory();
        //添加默认认证模式
        List<TokenGranter> tokenGranters = getDefaultTokenGranters(tokenServices, authorizationCodeServices, requestFactory);
        //增加自定义认证模式
        if (authenticationManager != null) {
            // 添加密码加图形验证码模式
            tokenGranters.add(new PwdImgCodeGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory, validateCodeProcessorHolder));
            // 添加openId模式
            tokenGranters.add(new OpenIdGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory));
            // 添加手机号加密码授权模式
            tokenGranters.add(new MobilePwdGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory));
        }
        return tokenGranters;
    }

    /**
     * 添加默认认证模式，这里的代码是参考AuthorizationServerEndpointsConfigurer类中的getDefaultTokenGranters方法
     *
     * @param tokenServices
     * @param authorizationCodeServices
     * @param requestFactory
     * @return
     */
    private List<TokenGranter> getDefaultTokenGranters(AuthorizationServerTokenServices tokenServices,
                                                       AuthorizationCodeServices authorizationCodeServices,
                                                       OAuth2RequestFactory requestFactory) {


        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        //添加授权码
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService,
                requestFactory));
        //添加刷新token
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory));
        //添加隐式授权
//        tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory));
        //添加客户端模式
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory));
        if (authenticationManager != null) {
            // 添加密码模式
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                    clientDetailsService, requestFactory));
        }
        return tokenGranters;
    }

    private OAuth2RequestFactory requestFactory() {
        return null;
    }


    private AuthorizationServerTokenServices tokenServices() {
        if (tokenServices != null) {
            return tokenServices;
        }
        this.tokenServices = createDefaultTokenServices();
        return tokenServices;
    }

    private DefaultTokenServices createDefaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(customRedisTokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(reuseRefreshToken);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setTokenEnhancer(tokenEnhancer());
        addUserDetailsService(tokenServices, this.myUserDetailsService);
        return tokenServices;
    }

    private TokenEnhancer tokenEnhancer() {
        if (tokenEnhancer != null) {
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            tokenEnhancerChain.setTokenEnhancers(tokenEnhancer);
            return tokenEnhancerChain;
        }
        return null;
    }

    private void addUserDetailsService(DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
    }

}
