package com.central.db.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.central.db.mybatis.MySqlInjector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author yujunjie
 */
public class MybatisPlusConfig {


    // 最新版
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//分页
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());//乐观锁
        return interceptor;
    }

    /**
     * 自定义 SqlInjector
     * 里面包含自定义的全局方法
     */
    @Bean
    public MySqlInjector myLogicSqlInjector() {
        return new MySqlInjector();
    }

}
