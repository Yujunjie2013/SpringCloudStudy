package org.junjie.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码存取器
 */
public interface ValidateCodeRepository {

    /**
     * 保存验证码
     *
     * @param request
     * @param code
     * @param validateCodeType
     */
    void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);

    /**
     * 获取验证码
     *
     * @param request
     * @param validateCodeType
     * @return
     */
    ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);

    /**
     * 获取验证码
     *
     * @param deviceId         deviceId
     * @param validateCodeType 类型
     * @return
     */
    ValidateCode get(String deviceId, ValidateCodeType validateCodeType);

    /**
     * 移除验证码
     *
     * @param deviceId
     * @param codeType
     */
    void remove(String deviceId, ValidateCodeType codeType);

}
