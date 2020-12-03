package com.junjie.product.service;


import com.central.db.service.IBaseService;
import com.junjie.product.entity.TbProduct;

public interface IProductService extends IBaseService<TbProduct> {
    TbProduct findById(Long id);

    Integer update(TbProduct tbProduct);

    void delete(Long id);
}
