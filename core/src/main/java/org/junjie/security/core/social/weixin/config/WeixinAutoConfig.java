/**
 *
 */
package org.junjie.security.core.social.weixin.config;

import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.properties.WeixinProperties;
import org.junjie.security.core.social.weixin.connect.WeixinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/**
 * 微信登录配置
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "org.junjie.social.weixin", name = "appId")
public class WeixinAutoConfig extends SocialConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;


    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(this.createConnectionFactory());
    }

    protected ConnectionFactory<?> createConnectionFactory() {
        WeixinProperties weixinConfig = securityProperties.getSocial().getWeixin();
        return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
                weixinConfig.getAppSecret());
    }

//	@Bean({"connect/weixinConnect", "connect/weixinConnected"})
//	@ConditionalOnMissingBean(name = "weixinConnectedView")
//	public View weixinConnectedView() {
//		return new ImoocConnectView();
//	}

}
