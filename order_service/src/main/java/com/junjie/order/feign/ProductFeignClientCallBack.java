package com.junjie.order.feign;

import com.junjie.product.entity.TbProduct;
import org.springframework.stereotype.Component;

@Component
public class ProductFeignClientCallBack implements ProductFeignClient {
    @Override
    public TbProduct findById(Long id) {
        TbProduct tbProduct = new TbProduct();
        tbProduct.setProductName("我是通过Feign降级的");
        return tbProduct;
    }
}
