package org.junjie.security.core.authentication;

import org.junjie.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    protected AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;

    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

//        .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)//将自定义的图形验证码加到用户密码过滤器之前
//                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)//将自定义的图形验证码加到用户密码过滤器之前
//                .formLogin()
//                //                .loginPage("/imooc-sign.html")//指定了跳转到登录页面的请求URL,这里的静态界面一定要放到static目录下，否则找不到
//                .loginPage("/authentication/require")//指定了跳转到登录页面的请求URL,这里的接口在BrowsersecurityController里
//                .loginProcessingUrl("/authentication/form")//自定义的登录url，UsernamePasswordAuthenticationFilter来处理
//                .successHandler(browserAuthenticationSuccessHandler)//自定义成功处理器
//                .failureHandler(browserAuthenticationFailureHandler)//自定义失败处理器
    }
}
