package com.junjie.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@MapperScan(basePackages = {"com.junjie.order.dao"})
@SpringBootApplication
public class OrderApplication {
    /**
     * 使用RestTemplate调用远程服务
     * 使用负载均衡，添加此注解后可以使用Ribbon来实现服务调用
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        System.setProperty("log4j2File", "order");
        SpringApplication.run(OrderApplication.class, args);
    }
}
