package org.junjie.security.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import org.junjie.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import org.junjie.security.core.authorize.AuthorizeConfigManager;
import org.junjie.security.core.authorize.AuthorizeConfigProvider;
import org.junjie.security.core.authorize.CodeAuthorizeConfigProvider;
import org.junjie.security.core.authorize.CoreAuthorizaConfigManager;
import org.junjie.security.core.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//使配置的ConfigurationProperties生效
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizeConfigManager authorizeConfigManager() {
        return new CoreAuthorizaConfigManager();
    }

    @Bean
    public AuthorizeConfigProvider authorizeConfigProvider() {
        return new CodeAuthorizeConfigProvider();
    }


}
