package com.junjie.common.config.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.junjie.common.config.mybatis.method.*;

import java.util.List;

/**
 * 自定义sql注入器
 *
 * @author yujunjie
 */
public class MySqlInjector extends DefaultSqlInjector {
    /**
     * 如果只需增加方法，保留MP自带方法
     * 可以super.getMethodList() 再add
     *
     * @return
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
//        methodList.add(new FindAllListMethod());
//        methodList.add(new FindByIdMethod());
//        methodList.add(new UpdateByIdMethod());
//        methodList.add(new FindPageMethod());
//        methodList.add(new FindListMethod());
        methodList.add(new SelectOneLimit());
        return methodList;
    }
}
