package com.central.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * 自定义baseMapper、此类总方法不能随意删除；对应{@link com.central.db.mybatis.method}包下对应的所有方法实例
 *
 * @param <T>
 * @author yujunjie
 */
public interface IBaseMapper<T> extends BaseMapper<T> {
    T selectOneLimit(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
}
