package org.junjie.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码处理器
 */
public interface ValidateCodeProcessor {

    /**
     * 创建校验码
     *
     * @param request
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     *
     * @param servletWebRequest
     * @throws Exception
     */
    void validate(ServletWebRequest servletWebRequest);

    /**
     * 校验验证码
     *
     * @param deviceId deviceId
     * @param code     请求的验证码
     * @param codeType 验证码类型
     */
    void validate(String deviceId, String code, ValidateCodeType codeType);
}
