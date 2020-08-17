package com.junjie.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.junjie.common.annotation.RedisLock;
import com.junjie.common.config.mybatis.BaseServiceImpl;
import com.junjie.product.dao.IProductDao;
import com.junjie.product.entity.TbProduct;
import com.junjie.product.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl extends BaseServiceImpl<IProductDao, TbProduct> implements IProductService {

    @Override
    @RedisLock(key = "'test'+#id")
    public TbProduct findById(Long id) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return getById(id);

    }

    @Override
    public Integer update(TbProduct tbProduct) {
        update(tbProduct, Wrappers.emptyWrapper());
        return 1;
    }

    @Override
    public void delete(Long id) {
        removeById(id);
    }
}
