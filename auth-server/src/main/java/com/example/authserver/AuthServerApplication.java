package com.example.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
//@SpringBootApplication(scanBasePackages = {"com.example.authserver", "com.junjie.common", "org.junjie.security.*"})
public class AuthServerApplication {

    public static void main(String[] args) {
        System.setProperty("log4j2File", "auth");
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
