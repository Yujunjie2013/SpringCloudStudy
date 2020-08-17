package com.junjie.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.junjie"})
@EntityScan("com.junjie.product.entity")
@ComponentScan(basePackages = {"com.junjie"})
@MapperScan(basePackages = {"com.junjie.product.dao"})
public class ProductApplication {

    public static void main(String[] args) {
        System.setProperty("log4j2File", "product");
        SpringApplication.run(ProductApplication.class, args);
    }
}
