package com.central.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yujunjie
 */
public interface IBaseService<T> extends IService<T> {
    T getOneLimit(Wrapper<T> queryWrapper);
//    /**
//     * 查询所有，无视逻辑删除
//     *
//     * @return 集合
//     */
//    List<T> findAllList();
//
//    /**
//     * 查询所有满足条件的数据（无视逻辑删除）
//     *
//     * @param queryWrapper 查询条件
//     * @return list
//     */
//    List<T> findList(Wrapper<T> queryWrapper);
//
//    /**
//     * 根据id查询数据，（无视逻辑删除状态）
//     *
//     * @param id 主键id
//     * @return 实体
//     */
//    T findById(Serializable id);
//
//    /**
//     * 根据id更新数据
//     *
//     * @param entity 根据主键id更新数据，需要更新的实体类 （无视逻辑删除状态）
//     * @return int
//     */
//    boolean updateById2Entity(T entity);
//
//    /**
//     * 根据 entity 条件，查询全部记录（并翻页，无视逻辑删除）
//     * (考虑逻辑删除)
//     *
//     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
//     * @param queryWrapper 实体对象封装操作类（可以为 null）
//     * @return page
//     */
//    Page<T> findPage(IPage<T> page, Wrapper<T> queryWrapper);
}
