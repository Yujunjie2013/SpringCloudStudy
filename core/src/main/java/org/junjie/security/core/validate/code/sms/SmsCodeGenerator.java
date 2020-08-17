package org.junjie.security.core.validate.code.sms;

import org.apache.commons.lang.RandomStringUtils;
import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.validate.code.ValidateCode;
import org.junjie.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码生成器
 */
//@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest servletWebRequest) {
        String code = RandomStringUtils.random(securityProperties.getCode().getSms().getLength(),false,true);
        return new ValidateCode(code, securityProperties.getCode().getImage().getExpireIn());
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}
