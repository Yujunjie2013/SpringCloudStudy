package com.junjie.common.config;

import com.alibaba.druid.filter.logging.Log4jFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
@Component
@Configuration
@Slf4j
public class SqlCostInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(SqlCostInterceptor.class);

    @Override
    public Object intercept(Invocation arg0) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return arg0.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long sqlCost = endTime - startTime;
            logger.info("执行耗时 : [" + sqlCost + "ms ] ");
        }
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties arg0) {
        // TODO Auto-generated method stub

    }

}
