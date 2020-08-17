package org.junjie.security.app.social.impl;

import org.apache.commons.lang.StringUtils;
import org.junjie.security.app.exception.AppSecretException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

@Component
public class AppSignUpUtils {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private UsersConnectionRepository usersConnectionRepository;
    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    public void saveConnectionData(WebRequest request, ConnectionData connectionData) {
        redisTemplate.opsForValue().set(getKey(request), connectionData, 10, TimeUnit.MINUTES);
    }

    public void doPostSignUp(WebRequest request, String userId) {
        String key = getKey(request);
        if (!redisTemplate.hasKey(key)) {
            throw new AppSecretException("无法找到缓存的用户社交账号信息");
        }
        ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
        Connection<?> connection = connectionFactory.createConnection(connectionData);
        usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);
        redisTemplate.delete(key);
    }


    private String getKey(WebRequest request) {
        String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new AppSecretException("设备ID参数不能为空");
        }
        return "imooc:security:social.connect." + deviceId;
    }

}
