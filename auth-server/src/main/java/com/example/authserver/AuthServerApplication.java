package com.example.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.junjie.common","org.junjie.security.*", "com.example.authserver"})
public class AuthServerApplication {

    public static void main(String[] args) {
        System.setProperty("log4j2File", "auth");
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
