package com.junjie.product.dao;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junjie.common.bean.TbProduct;

public interface TbProductMapper extends BaseMapper<TbProduct> {


    List<TbProduct> findByPriceGreaterThan(@Param("minPrice")BigDecimal minPrice);

    List<TbProduct> findByPriceAfter(@Param("minPrice")BigDecimal minPrice);





}