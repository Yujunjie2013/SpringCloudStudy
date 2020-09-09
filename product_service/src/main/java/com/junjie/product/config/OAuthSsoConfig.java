package com.junjie.product.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;

@Configuration
@EnableOAuth2Sso // 开启 Sso 功能
public class OAuthSsoConfig extends WebSecurityConfigurerAdapter {
//    @Value("${security.oauth2.sso.login-path:}")
//    private String loginPath;

//    @Resource
//    private LogoutSuccessHandler ssoLogoutSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable()
                .logout();
//                .logoutSuccessHandler(ssoLogoutSuccessHandler);
//        if (StringUtils.isNotEmpty(loginPath)) {
//            http.formLogin().loginProcessingUrl(loginPath);
//        }
    }
}