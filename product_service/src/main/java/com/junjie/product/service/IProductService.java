package com.junjie.product.service;


import com.junjie.common.config.mybatis.IBaseService;
import com.junjie.product.entity.TbProduct;

public interface IProductService extends IBaseService<TbProduct> {
    TbProduct findById(Long id);

    Integer update(TbProduct tbProduct);

    void delete(Long id);
}
