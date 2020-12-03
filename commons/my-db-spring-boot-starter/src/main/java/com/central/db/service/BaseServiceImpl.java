package com.central.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.central.db.mapper.IBaseMapper;

/**
 * 基类service，默认实现自定义mapper中的方法
 * Base实现类（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 *
 * @author yujunjie
 */
public abstract class BaseServiceImpl<M extends IBaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

    @Override
    public T getOneLimit(Wrapper<T> queryWrapper) {
        return null;
    }

    @Override
    public T getOne(Wrapper<T> queryWrapper) {
        return baseMapper.selectOneLimit(queryWrapper);
    }
}
