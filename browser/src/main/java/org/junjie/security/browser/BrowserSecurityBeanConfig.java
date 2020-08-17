package org.junjie.security.browser;

import org.junjie.security.browser.logout.DefaultLogoutSuccessHandler;
import org.junjie.security.browser.session.DefaultExpireSessionStrategy;
import org.junjie.security.browser.session.DefaultInvalidSessionStrategy;
import org.junjie.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@Configuration
public class BrowserSecurityBeanConfig {
    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new DefaultInvalidSessionStrategy(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new DefaultExpireSessionStrategy(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new DefaultLogoutSuccessHandler(securityProperties);
    }
}
