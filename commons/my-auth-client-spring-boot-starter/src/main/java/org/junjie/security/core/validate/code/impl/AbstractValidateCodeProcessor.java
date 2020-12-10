package org.junjie.security.core.validate.code.impl;

import org.apache.commons.lang3.StringUtils;
import org.junjie.security.core.properties.SecurityConstants;
import org.junjie.security.core.validate.code.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractValidateCodeProcessor<V extends ValidateCode> implements ValidateCodeProcessor {
    //收集系统中所有ValidateCodeGenerator接口的实现,名字是key，实例是value放入map
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;

    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    @Override
    public void create(ServletWebRequest request) throws Exception {
        V validateCode = generate(request);
        if (validateCode == null) {
            throw new NullPointerException("验证码为生成为null");
        }
        save(request, validateCode);
        send(request, validateCode);
    }

    /**
     * 保存验证码,这里保存到了Session中
     *
     * @param request      请求
     * @param validateCode 验证码
     */
    protected void save(ServletWebRequest request, V validateCode) {
        //不论传入的是图形验证码还是短信验证码都只存储验证码和时间
        ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
        validateCodeRepository.save(request, code, getValidateCodeType());
    }

    /**
     * 发送验证码，具体实现应该是业务模块根据短信运营商去实现
     *
     * @param request      请求
     * @param validateCode 验证码
     */
    protected abstract void send(ServletWebRequest request, V validateCode) throws IOException, ServletRequestBindingException;

    /**
     * 实际调用生成验证码的逻辑
     */
    @SuppressWarnings("unchecked")
    private V generate(ServletWebRequest request) throws NullPointerException {
        String type = getProcessorType(request);
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(type + SecurityConstants.CODE_GENERATOR_ID);
        if (validateCodeGenerator == null) {
            throw new NullPointerException("验证码生成器为空，无法生成验证码");
        }
        return (V) validateCodeGenerator.generate(request);
    }

    protected String getProcessorType(ServletWebRequest request) {
        //图形验证码/code/image
        //短信验证码/code/sms
        //所以截取后分别是image和sms
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), "/code/");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(ServletWebRequest servletWebRequest) {
        ValidateCodeType codeType = getValidateCodeType();
        String deviceId = servletWebRequest.getHeader("deviceId");
        V codeInRepository = (V) validateCodeRepository.get(deviceId, codeType);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),
                    codeType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        check(deviceId, codeType, codeInRepository, codeInRequest);

    }

    @Override
    public void validate(String deviceId, String code, ValidateCodeType codeType) {
        V codeInRepository = (V) validateCodeRepository.get(deviceId, codeType);
        check(deviceId, codeType, codeInRepository, code);
    }

    private void check(String deviceId, ValidateCodeType codeType, V codeInRepository, String codeInRequest) {
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(codeType + "验证码的值不能为空");
        }

        if (codeInRepository == null) {
            throw new ValidateCodeException(codeType + "验证码不存在");
        }

        if (codeInRepository.isExpried()) {
            validateCodeRepository.remove(deviceId, codeType);
            throw new ValidateCodeException(codeType + "验证码已过期");
        }

        if (!StringUtils.equals(codeInRepository.getCode(), codeInRequest)) {
            throw new ValidateCodeException(codeType + "验证码不匹配");
        }
        validateCodeRepository.remove(deviceId, codeType);
    }

    /**
     * 根据请求的url获取校验码的类型
     *
     * @return 验证码类型
     */
    private ValidateCodeType getValidateCodeType() {
        //getClass().getSimpleName()取到的值可能为SmsValidateCodeProcessor、ImageValidateCodeProcessor
        //所以这里返回的值可能是SMS、IMAGE
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "ValidateCodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

}
