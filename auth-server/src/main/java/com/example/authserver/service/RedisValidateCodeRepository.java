package com.example.authserver.service;

import com.central.redis.template.RedisRepository;
import org.apache.commons.lang3.StringUtils;
import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.validate.code.ValidateCode;
import org.junjie.security.core.validate.code.ValidateCodeException;
import org.junjie.security.core.validate.code.ValidateCodeRepository;
import org.junjie.security.core.validate.code.ValidateCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
        redisRepository.setExpire(buildKey(request, validateCodeType), code, securityProperties.getCode().getTotalExpireIn(), TimeUnit.SECONDS);
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
        Object value = redisRepository.get(buildKey(request, validateCodeType));
        if (validateCodeType == null) {
            return null;
        }
        return (ValidateCode) value;
    }

    public ValidateCode get(String deviceId, ValidateCodeType validateCodeType) {
        Object value = redisRepository.get(generaterKey(deviceId, validateCodeType));
        if (validateCodeType == null) {
            return null;
        }
        return (ValidateCode) value;
    }

    @Override
    public void remove(String deviceId, ValidateCodeType codeType) {
        redisRepository.del(generaterKey(deviceId, codeType));
    }

    private String buildKey(ServletWebRequest request, ValidateCodeType type) {
        String deviceId = request.getHeader("deviceId");
        return generaterKey(deviceId, type);
    }

    private String generaterKey(String deviceId, ValidateCodeType type) {
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }
        return "code:" + type.toString().toLowerCase() + ":" + deviceId;
    }
}
