package com.junjie.product.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.central.db.service.IBaseService;
import com.junjie.product.entity.TbProduct;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;

import java.util.List;

public interface IProductService extends IBaseService<TbProduct> {

    TbProduct add(TbProduct tbProduct);

    TbProduct findById(Long id);

    TbProduct update(TbProduct tbProduct);

    void delete(Long id);

    IPage<TbProduct> getList(Long page, Long pageSize);
}
