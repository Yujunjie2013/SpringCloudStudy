package com.junjie.order.feign;

import com.junjie.product.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 什么需要调用的微服务名称
 * name:服务提供者的名称
 */
@FeignClient(name = "service-product", fallback = ProductFeignClientCallBack.class)
public interface ProductFeignClient {
    /**
     * 配置需要调用的微服务接口
     */
    @GetMapping("/product/{id}")
    Product findById(@PathVariable("id") Long id);
}
