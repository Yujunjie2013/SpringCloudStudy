package org.junjie.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码生成器接口，子模块可自行实现验证码生成逻辑
 */
public interface ValidateCodeGenerator {
    /**
     * 生成图形验证码
     *
     * @param servletWebRequest
     * @return
     */
    ValidateCode generate(ServletWebRequest servletWebRequest);
}
