package org.junjie.security.core.config;

import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.token.store.CustomRedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


public class TokenStoreConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnProperty(prefix = "org.junjie.oauth2", name = "storeType", havingValue = "redis", matchIfMissing = true)
    public TokenStore redisTokenStore() {
        return new CustomRedisTokenStore(redisConnectionFactory, securityProperties);
    }

    @Configuration
    @ConditionalOnProperty(prefix = "org.junjie.oauth2", name = "storeType", havingValue = "jwt")
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

//        @Bean
//        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
//        public TokenEnhancer jwtTokenEnhancer() {
//            return new AppJwtTokenEnhancer();
//        }
    }
}
