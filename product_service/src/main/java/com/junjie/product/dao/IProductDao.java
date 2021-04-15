package com.junjie.product.dao;

import com.central.db.mapper.IBaseMapper;
import com.junjie.product.entity.TbProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IProductDao extends IBaseMapper<TbProduct> {
    TbProduct findByPrice(@Param("price") BigDecimal price);
}
