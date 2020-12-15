package com.example.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@SpringBootApplication
//@EnableFeignClients(basePackages = {"com.junjie.common.feign"})
@EnableFeignClients
@EnableDiscoveryClient
public class AuthServerApplication {

    public static void main(String[] args) {

        System.setProperty("log4j2File", "auth");
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
