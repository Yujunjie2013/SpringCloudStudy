package com.junjie.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.junjie"})
@EntityScan("com.junjie.product.entity")
@MapperScan(basePackages = {"com.junjie.*.dao"})
@EnableOAuth2Sso //配置接入 SSO 功能，可以看看 SsoSecurityConfigurer 类。
public class ProductApplication {

    public static void main(String[] args) {
        System.setProperty("log4j2File", "product");
        SpringApplication.run(ProductApplication.class, args);
    }
}
