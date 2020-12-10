package com.example.authserver.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

import java.nio.charset.StandardCharsets;

/**
 * JSON格式存储
 */
public class JsonRedisTokenStoreSerializationStrategy implements RedisTokenStoreSerializationStrategy {

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return JSON.parseObject(bytes, clazz);
    }

    @Override
    public String deserializeString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(Object object) {
        if (object == null) {
            return new byte[0];
        }

        try {
            return JSON.toJSONBytes(object, SerializerFeature.WriteClassName,
                    SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @Override
    public byte[] serialize(String data) {
        if (data == null || data.length() == 0) {
            return new byte[0];
        }
        return data.getBytes(StandardCharsets.UTF_8);
    }
}
