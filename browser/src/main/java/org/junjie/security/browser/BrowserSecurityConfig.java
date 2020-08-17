package org.junjie.security.browser;

import org.junjie.security.browser.session.DefaultExpireSessionStrategy;
import org.junjie.security.core.authentication.AbstractChannelSecurityConfig;
import org.junjie.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import org.junjie.security.core.authorize.AuthorizeConfigManager;
import org.junjie.security.core.properties.SecurityConstants;
import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        applyPasswordAuthenticationConfig(http);
        http.apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(springSocialConfigurer)
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                .sessionManagement()
//                .invalidSessionUrl("/session/invalid")//同invalidSessionStrategy之间二者取其一
                .invalidSessionStrategy(invalidSessionStrategy)////session过期处理策略
                .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSession())//最大登录数，同一账号一个地方登录后其他地方登录会挤掉线
                .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionPreventsLogin())//设置成true，表示当当前用户session是可用时，在其他地方无法登录
                .expiredSessionStrategy(sessionInformationExpiredStrategy)//session最大并发处理策略
                .and()
                .and()
                .logout()
                .logoutUrl("/signOut")
//                .logoutSuccessUrl("/imooc-logout.html")
                .logoutSuccessHandler(logoutSuccessHandler)//跟logoutSuccessUrl二者取其一
                .deleteCookies("JSESSIONID")
                .and()
                .csrf()//CSRF攻击防御关了
                .disable();//都需要认证
        authorizeConfigManager.config(http.authorizeRequests());
    }

}
