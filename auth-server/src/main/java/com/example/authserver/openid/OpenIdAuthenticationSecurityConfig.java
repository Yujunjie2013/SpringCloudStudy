package com.example.authserver.openid;

import com.example.authserver.authentication.AppAuthenticationFailureHandler;
import com.example.authserver.authentication.AppAuthenticationSuccessHandler;
import com.example.authserver.authentication.openid.OpenIdAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * openId
 */
@Component
public class OpenIdAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private SocialUserDetailsService userDetailsService;

    @Autowired
    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
    @Autowired
    private AppAuthenticationFailureHandler appAuthenticationFailureHandler;

    @Override
    public void configure(HttpSecurity http) {
        //openId provider
//        OpenIdAuthenticationProvider provider = new OpenIdAuthenticationProvider(userDetailsService);
//        http.authenticationProvider(provider);


        OpenIdAuthenticationFilter OpenIdAuthenticationFilter = new OpenIdAuthenticationFilter();
        OpenIdAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        OpenIdAuthenticationFilter.setAuthenticationSuccessHandler(appAuthenticationSuccessHandler);
        OpenIdAuthenticationFilter.setAuthenticationFailureHandler(appAuthenticationFailureHandler);

        OpenIdAuthenticationProvider OpenIdAuthenticationProvider = new OpenIdAuthenticationProvider(userDetailsService);

        http.authenticationProvider(OpenIdAuthenticationProvider)
                .addFilterAfter(OpenIdAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
