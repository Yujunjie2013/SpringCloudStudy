package com.junjie.order.controller;

import com.junjie.common.bean.Result;
import com.junjie.order.entity.TbOrder;
import com.junjie.order.feign.ProductFeignClient;
import com.junjie.order.service.ITbOrderService;
import com.junjie.product.entity.TbProduct;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ITbOrderService iTbOrderService;

    @Autowired
    private RestTemplate restTemplate;
    @Resource
    private ProductFeignClient productFeignClient;

    @PostMapping("/add")
    public Result addOrder(@RequestBody TbOrder tbOrder) {
        boolean save = iTbOrderService.save(tbOrder);
        return save ? Result.succeed(tbOrder) : Result.failed();
    }

    //    通过ribbon方式调用
    @GetMapping("/buy/{id}")
    @HystrixCommand(fallbackMethod = "hystrixProduct")
    public Result findById(@PathVariable Long id) {
        //没有用ribbon之前
//        Product product = restTemplate.getForObject("http://127.0.0.1:9001/product/" + id, Product.class);
        //使用ribbon的方式，只需要将服务名service-product和需要调用的地址写上就可以，不在需要使用ip的方式调用
        Result tbProduct = restTemplate.getForObject("http://service-product/product/" + id, Result.class);
        return tbProduct;
    }

    //通过Feign方式调用
    @GetMapping("/buyFeign/{id}")
    public Result<TbProduct> findByIdFeign(@PathVariable Long id) {
        Result result = productFeignClient.findById(id);
        return result;
    }

    /**
     * 熔断的方法、返回值和、参数必须跟原配一样
     *
     * @param id
     * @return
     */
    public TbProduct hystrixProduct(Long id) {
        TbProduct tbProduct = new TbProduct();
        tbProduct.setProductName("我被降级啦");
        return tbProduct;
    }

    //    通过restTemplateribbon方式调用,结合Hystrix
//    @GetMapping("/buy/{id}")
//    @HystrixCommand(fallbackMethod = "hystrixProduct")
//    public Product findById(@PathVariable Long id) {
//        //没有用ribbon之前
////        Product product = restTemplate.getForObject("http://127.0.0.1:9001/product/" + id, Product.class);
//        //使用ribbon的方式，只需要将服务名service-product和需要调用的地址写上就可以，不在需要使用ip的方式调用
//        Product product = restTemplate.getForObject("http://service-product/product/" + id, Product.class);
//        return product;
//    }


}
