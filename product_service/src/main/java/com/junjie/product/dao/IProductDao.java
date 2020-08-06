package com.junjie.product.dao;

import com.junjie.common.config.mybatis.IBaseMapper;
import com.junjie.product.entity.TbProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductDao extends IBaseMapper<TbProduct> {
}
