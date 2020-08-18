package org.junjie.security.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AAppSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 数据源 DataSource
     */
    @Autowired
    private DataSource dataSource;
//    @Autowired
//    private SecurityProperties securityProperties;
//    @Autowired
//    protected AuthenticationSuccessHandler authenticationSuccessHandler;
//    @Autowired
//    protected AuthenticationFailureHandler authenticationFailureHandler;
//    @Autowired
//    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
//    @Autowired
//    private SpringSocialConfigurer springSocialConfigurer;
//    @Autowired
//    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
//    @Autowired
//    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private AuthorizeConfigManager authorizeConfigManager;


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.authenticationManager(authenticationManager);
//    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//
//        http.formLogin()
//                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
//                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
//                .successHandler(authenticationSuccessHandler)
//                .failureHandler(authenticationFailureHandler);
//
////        applyPasswordAuthenticationConfig(http);
//
//        http
//                .apply(validateCodeSecurityConfig)
//                .and()
//                .apply(smsCodeAuthenticationSecurityConfig)
//                .and()
//                .apply(springSocialConfigurer)
//                .and()
//                .apply(openIdAuthenticationSecurityConfig)
//                .and()
//                .csrf()//CSRF攻击防御关了
//                .disable();//都需要认证
//        authorizeConfigManager.config(http.authorizeRequests());
//    }
}
