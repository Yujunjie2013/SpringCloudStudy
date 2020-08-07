package com.junjie.product.dao;

import com.junjie.common.config.mybatis.IBaseMapper;
import com.junjie.product.entity.TbProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IProductDao extends IBaseMapper<TbProduct> {
    TbProduct findByPrice(@Param("price") BigDecimal price);
}
