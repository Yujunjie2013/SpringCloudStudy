package com.junjie.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class KeyResolverConfig {

    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> {
            //根据请求路径限流
            return Mono.just(exchange.getRequest().getPath().toString());
        };
    }

//    @Bean
//    public KeyResolver paramsKeyResolver() {
//        return exchange -> {
//            //根据请求参数限流
//            return Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
//        };
//    }
}
