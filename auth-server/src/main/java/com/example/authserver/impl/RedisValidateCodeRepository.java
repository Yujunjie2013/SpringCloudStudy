package com.example.authserver.impl;

import com.central.redis.template.RedisRepository;
import org.apache.commons.lang3.StringUtils;
import org.junjie.security.core.validate.code.ValidateCode;
import org.junjie.security.core.validate.code.ValidateCodeException;
import org.junjie.security.core.validate.code.ValidateCodeRepository;
import org.junjie.security.core.validate.code.ValidateCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
//        redisTemplate.opsForValue().set(buildKey(request, validateCodeType), code, 30, TimeUnit.MINUTES);
        redisRepository.setExpire(buildKey(request, validateCodeType), code, 30, TimeUnit.MINUTES);
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
//        Object value = redisTemplate.opsForValue().get(buildKey(request, validateCodeType));
        Object value = redisRepository.get(buildKey(request, validateCodeType));
        if (validateCodeType == null) {
            return null;
        }
        return (ValidateCode) value;
    }

    public ValidateCode get(String deviceId, ValidateCodeType validateCodeType) {
//        Object value = redisTemplate.opsForValue().get(generaterKey(deviceId, validateCodeType));
        Object value = redisRepository.get(generaterKey(deviceId, validateCodeType));
        if (validateCodeType == null) {
            return null;
        }
        return (ValidateCode) value;
    }

    @Override
    public void remove(String deviceId, ValidateCodeType codeType) {
//        redisTemplate.delete(generaterKey(deviceId, codeType));
        redisRepository.del(generaterKey(deviceId, codeType));
    }

    /**
     * @param request
     * @param type
     * @return
     */
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
