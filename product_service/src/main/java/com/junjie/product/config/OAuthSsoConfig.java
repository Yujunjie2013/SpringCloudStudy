package com.junjie.product.config;

import org.junjie.security.core.config.DefaultSecurityHandlerConfig;
import org.junjie.security.core.support.RemoteTokenServicesExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Import({DefaultSecurityHandlerConfig.class})
@Configuration
//@EnableOAuth2Sso // 开启 Sso 功能
@EnableResourceServer
public class OAuthSsoConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Resource
    private OAuth2WebSecurityExpressionHandler expressionHandler;

    @Resource
    private OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;

    @Autowired
    private ResourceServerProperties resourceServerProperties;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore)
                .tokenServices(new RemoteTokenServicesExt(new RestTemplate(), resourceServerProperties))
                .stateless(true)
                .authenticationEntryPoint(authenticationEntryPoint)
                .expressionHandler(expressionHandler)
                .accessDeniedHandler(oAuth2AccessDeniedHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl = setHttp(http)
                .authorizeRequests()
                .antMatchers("/").authenticated()
//                .antMatchers(securityProperties.getIgnore().getUrls()).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest();
        setAuthenticate(authorizedUrl);
//
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and()
//                .httpBasic().disable()
//                .headers()
//                .frameOptions().disable()
//                .and()
//                .csrf().disable();
    }

    /**
     * url权限控制，默认是认证就通过，可以重写实现个性化
     *
     * @param authorizedUrl
     */
    public HttpSecurity setAuthenticate(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl) {
        return authorizedUrl.authenticated().and();
    }

    /**
     * 留给子类重写扩展功能
     *
     * @param http
     */
    public HttpSecurity setHttp(HttpSecurity http) {
        return http;
    }

}