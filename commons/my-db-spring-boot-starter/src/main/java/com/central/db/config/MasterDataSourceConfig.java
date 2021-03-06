package com.central.db.config;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.central.db.mybatis.MySqlInjector;
import com.central.db.mybatis.SqlCostInterceptor;
import com.central.db.mybatis.TimeMetaObjectHandler;
import com.central.db.properties.MybatisPlusAutoFillProperties;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author yujunjie
 */
@EnableConfigurationProperties(MybatisPlusAutoFillProperties.class)
public class MasterDataSourceConfig {
    private static final String MAPPER_LOCATION = "classpath*:mapper/**/*.xml";
    @Autowired
    private MybatisPlusAutoFillProperties autoFillProperties;

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(DataSource masterDataSource,
                                                     GlobalConfig globalConfig,
                                                     MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        Interceptor[] plugins = new Interceptor[]{new SqlCostInterceptor(), mybatisPlusInterceptor};
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
    public GlobalConfig globalConfiguration(MySqlInjector mySqlInjector) {
        GlobalConfig conf = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setLogicNotDeleteValue("0");
        dbConfig.setLogicDeleteValue("1");
        dbConfig.setLogicDeleteField("logicDelete");
        dbConfig.setUpdateStrategy(FieldStrategy.NOT_NULL);
        conf.setMetaObjectHandler(new TimeMetaObjectHandler(autoFillProperties));
        conf.setSqlInjector(mySqlInjector);
        conf.setDbConfig(dbConfig);
        return conf;
    }
}