package com.junjie.product.service.impl;

import com.junjie.common.annotation.RedisLock;
import com.junjie.product.dao.ProductDao;
import com.junjie.product.entity.Product;
import com.junjie.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    @RedisLock(key = "'test'+#id")
    public Product findById(Long id) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Optional<Product> optional = productDao.findById(id);
        if (optional.isPresent()) {
            Product product = optional.get();
            product.setPrice(product.getPrice().add(BigDecimal.valueOf(1)));
            productDao.save(product);
            return product;
        }
        return null;

    }

    @Override
    public void save(Product product) {
        productDao.save(product);
    }

    @Override
    public Integer update(Product product) {
        productDao.save(product);
        return 1;
    }

    @Override
    public void delete(Long id) {
        productDao.deleteById(id);
    }
}
