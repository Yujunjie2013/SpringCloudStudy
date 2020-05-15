package com.junjie.order.feign;

import com.junjie.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductFeignClientCallBack implements ProductFeignClient {
    @Override
    public Product findById(Long id) {
        Product product = new Product();
        product.setProductName("我是通过Feign降级的");
        return product;
    }
}
