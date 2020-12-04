package org.junjie.security.core.social.qq.config;

import org.junjie.security.core.properties.QQProperties;
import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.social.qq.connet.QQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

@Configuration
//这里表示只有配置了org.junjie.social.qq中的appId配置项，下面的配置才生效,否则该类不生效
@ConditionalOnProperty(prefix = "org.junjie.social.qq", name = "appId")
public class QQAutoConfig extends /*SocialAutoConfigurerAdapter*/SocialConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SecurityProperties securityProperties;

    public QQAutoConfig() {
    }

    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(this.createConnectionFactory());
    }

    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qq = securityProperties.getSocial().getQq();
        logger.info("加载配置---providerId:" + qq.getProviderId() + "--appId:" + qq.getAppId() + "---secret:" + qq.getAppSecret());
        QQConnectionFactory qqConnectionFactory = new QQConnectionFactory(qq.getProviderId(), qq.getAppId(), qq.getAppSecret());
        return qqConnectionFactory;
    }
}
