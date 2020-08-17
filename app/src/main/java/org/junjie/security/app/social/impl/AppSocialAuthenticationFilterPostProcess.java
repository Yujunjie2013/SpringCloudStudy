package org.junjie.security.app.social.impl;

import org.junjie.security.core.social.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class AppSocialAuthenticationFilterPostProcess implements SocialAuthenticationFilterPostProcessor {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    public void process(SocialAuthenticationFilter filter) {
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    }
}
