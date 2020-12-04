package org.junjie.security.core.social;

import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.social.jdbc.MyJdbcUsersConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.util.Assert;

import javax.sql.DataSource;

@EnableSocial
@Configuration
public class SocialConfig extends SocialConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DataSource dataSource;
    @Autowired
    private SecurityProperties securityProperties;
//    @Autowired
//    private ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;
    @Autowired(required = false)
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        MyJdbcUsersConnectionRepository jdbcUsersConnectionRepository = new MyJdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        if (connectionSignUp != null) {
            jdbcUsersConnectionRepository.setConnectionSignUp(connectionSignUp);
        }
//        jdbcUsersConnectionRepository.setTablePrefix("");//如果表名有前缀可以加上
        return jdbcUsersConnectionRepository;
    }

    @Bean
    public SpringSocialConfigurer springSocialConfigurer() {
        JunjieSpringSocialConfiger junjieSpringSocialConfiger = new JunjieSpringSocialConfiger(securityProperties.getBrowser().getSignUpUrl(), securityProperties.getSocial().getFilterProcessesUrl());
        junjieSpringSocialConfiger.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        return junjieSpringSocialConfiger;
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator));
    }

    //
    private static class SecurityContextUserIdSource implements UserIdSource {
        private Logger logger = LoggerFactory.getLogger(getClass());

        private SecurityContextUserIdSource() {
        }

        @Override
        public String getUserId() {
            logger.info("获取ID-->SecurityContextUserIdSource#getUserId()");
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            Assert.state(authentication != null, "Unable to get a ConnectionRepository: no user signed in");
            return authentication.getName();
        }
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new SecurityContextUserIdSource();
    }

    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }
}
