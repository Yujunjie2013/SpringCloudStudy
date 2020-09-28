package com.example.authserver.config;

import com.example.authserver.jwt.AppJwtTokenEnhancer;
import org.junjie.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenStoreConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnProperty(prefix = "org.junjie.oauth2", name = "storeType", havingValue = "redis")
    public TokenStore redisTokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
        return new CustomRedisTokenStore(redisConnectionFactory, securityProperties);
    }

    @Configuration
    @ConditionalOnProperty(prefix = "org.junjie.oauth2", name = "storeType", havingValue = "jwt", matchIfMissing = true)
    public static class JwtTokenConfig {

        @Autowired
        private SecurityProperties securityProperties;

        @Bean
        public TokenStore jwtTokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
            jwtAccessTokenConverter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
            return jwtAccessTokenConverter;
        }

        @Bean
        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer() {
            return new AppJwtTokenEnhancer();
        }
    }
}
