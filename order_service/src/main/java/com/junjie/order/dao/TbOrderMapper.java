package com.junjie.order.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.junjie.common.bean.TbOrder;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TbOrderMapper extends BaseMapper<TbOrder> {
    List<TbOrder> findAllList();

    TbOrder selectOneById(@Param("id") Integer id);

    @Select("select * from tb_order where id=#{id} ")
    TbOrder findOnById(@Param("id") Integer id);

}