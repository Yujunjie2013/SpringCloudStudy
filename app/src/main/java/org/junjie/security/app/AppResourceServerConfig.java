package org.junjie.security.app;

import org.junjie.security.app.authentication.openid.OpenIdAuthenticationSecurityConfig;
import org.junjie.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import org.junjie.security.core.authorize.AuthorizeConfigManager;
import org.junjie.security.core.properties.SecurityConstants;
import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableResourceServer
public class AppResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    protected AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.authenticationManager(authenticationManager);
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        http
                .apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(springSocialConfigurer)
                .and()
                .apply(openIdAuthenticationSecurityConfig)
                .and()
                .csrf()//CSRF攻击防御关了
                .disable();//都需要认证
        authorizeConfigManager.config(http.authorizeRequests());
    }
}
