package org.junjie.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码处理器
 */
public interface ValidateCodeProcessor {
    /**
     * 验证码放入Session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

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
     * @param code 请求的验证码
     * @param codeType 验证码类型
     */
    void validate(String deviceId, String code, ValidateCodeType codeType);
}
