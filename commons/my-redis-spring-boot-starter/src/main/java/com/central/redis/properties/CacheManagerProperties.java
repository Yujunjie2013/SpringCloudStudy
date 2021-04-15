package com.central.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "com.mycache-manager")
public class CacheManagerProperties {
    /**
     * Redis缓存配置集合
     */
    private List<CacheConfig> configs;
    /**
     * 没有指定缓存名称，默认的缓存有效期300秒=5分钟
     */
    private long defaultTime=300;
    @Setter
    @Getter
    public static class CacheConfig {
        /**
         * 指定的缓存名称
         */
        private String cacheName;
        /**
         * 指定缓存名称的过期时间，单位秒
         */
        private long second = 60;
    }
}