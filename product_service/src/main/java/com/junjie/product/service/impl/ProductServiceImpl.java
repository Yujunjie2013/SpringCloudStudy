package com.junjie.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.db.service.BaseServiceImpl;
import com.junjie.common.annotation.RedisLock;
import com.junjie.product.dao.IProductDao;
import com.junjie.product.entity.TbProduct;
import com.junjie.product.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * yml中配置的缓存productServiceImpl有效期是20s
 * 在方法上的缓存如果没有指定缓存名则默认是20s有效期
 */
@Service
@Slf4j
@CacheConfig(cacheNames = ProductServiceImpl.productServiceImpl)
public class ProductServiceImpl extends BaseServiceImpl<IProductDao, TbProduct> implements IProductService {
    private final String Second10 = "Second10";
    private final String Hour2 = "Hour2";
    public static final String productServiceImpl = "ProductServiceImpl";

    /**
     * yml中配置的缓存名称Second10 其缓存有效期是10秒钟
     *
     * @param id
     * @return
     */
    @Override
    @RedisLock(key = "'test'+#id")
    @Cacheable(cacheNames = Second10, key = "'tb_'+#id", unless = "#result==null")
    public TbProduct findById(Long id) {
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return getById(id);

    }

    @CachePut(key = "'tb_'+#tbProduct.id", unless = "#result==0")
    @Override
    public TbProduct update(TbProduct tbProduct) {
        updateById(tbProduct);
        return tbProduct;
    }


    @CacheEvict(key = "'tb_'+#id")
    @Override
    public void delete(Long id) {
        removeById(id);
    }

    /**
     * 在yml中配置的缓存名称Hour2 的有效期是2小时
     *
     * @param tbProduct
     * @return
     */
    @CachePut(cacheNames = Hour2, key = "'tb_'+#tbProduct.id")
    @Override
    public TbProduct add(TbProduct tbProduct) {
        save(tbProduct);
        return tbProduct;
    }


    @Override
    public IPage<TbProduct> getList(Long page, Long pageSize) {
        Page<TbProduct> objectPage = new Page<>(page, pageSize);
        return page(objectPage);
    }
}
