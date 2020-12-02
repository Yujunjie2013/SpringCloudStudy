package com.junjie.common.config;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.junjie.common.config.mybatis.MySqlInjector;
import com.junjie.common.config.mybatis.TimeMetaObjectHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author yujunjie
 */
@Configuration
public class MasterDataSourceConfig {
    private static final String MAPPER_LOCATION = "classpath*:mapper/**/*.xml";

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(DataSource masterDataSource,
                                                     GlobalConfig globalConfig)
            throws Exception {
//        BaseTypeHandler
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
//        Interceptor[] plugins = new Interceptor[]{pageHelper(), new SqlCostInterceptor()};
        Interceptor[] plugins = new Interceptor[]{new SqlCostInterceptor()};
        sessionFactory.setPlugins(plugins);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        sessionFactory.setConfiguration(configuration);
        sessionFactory.setGlobalConfig(globalConfig);
        return sessionFactory.getObject();
    }

    /**
     * mybatis-plus 全局配置
     *
     * @return
     */
    @Bean
    public GlobalConfig globalConfiguration() {
        GlobalConfig conf = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setLogicNotDeleteValue("0");
        dbConfig.setLogicDeleteValue("1");
        dbConfig.setLogicDeleteField("logicDelete");
        dbConfig.setUpdateStrategy(FieldStrategy.NOT_NULL);
        conf.setMetaObjectHandler(new TimeMetaObjectHandler());
        conf.setSqlInjector(new MySqlInjector());
        conf.setDbConfig(dbConfig);
        return conf;
    }
}