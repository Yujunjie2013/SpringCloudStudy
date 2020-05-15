package com.junjie.product.service;


import com.junjie.product.entity.Product;

public interface ProductService {
    Product findById(Long id);

    void save(Product product);

    Integer update(Product product);

    void delete(Long id);
}
