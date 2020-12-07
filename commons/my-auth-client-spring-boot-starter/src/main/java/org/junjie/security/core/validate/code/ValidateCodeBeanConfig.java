package org.junjie.security.core.validate.code;

import org.junjie.security.core.properties.SecurityConstants;
import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.validate.code.image.ImageCodeGenerator;
import org.junjie.security.core.validate.code.image.ImageValidateCodeProcessor;
import org.junjie.security.core.validate.code.sms.DefaultSmsCodeSender;
import org.junjie.security.core.validate.code.sms.SmsCodeGenerator;
import org.junjie.security.core.validate.code.sms.SmsCodeSender;
import org.junjie.security.core.validate.code.sms.SmsValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 验证码bean配置类
 */
//@Configuration
public class ValidateCodeBeanConfig {
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 验证码生成器，如果用户没有定义imageCodeGenerator时
     *
     * @return 图形验证码生成器
     */
    @Bean
    @ConditionalOnMissingBean(name = SecurityConstants.IMAGE_CODE_GENERATOR_ID)
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        imageCodeGenerator.setSecurityProperties(securityProperties);
        return imageCodeGenerator;
    }

    /**
     * 短信验证码生成器，如果用户没有定义imageCodeGenerator时
     *
     * @return 短信验证码生成器
     */
    @Bean
    @ConditionalOnMissingBean(name = SecurityConstants.SMS_CODE_GENERATOR_ID)
    public ValidateCodeGenerator smsCodeGenerator() {
        SmsCodeGenerator imageCodeGenerator = new SmsCodeGenerator();
        imageCodeGenerator.setSecurityProperties(securityProperties);
        return imageCodeGenerator;
    }

    /**
     * 验证码发送接口，当没有SmsCodeSender接口的实现类时，使用默认提供的
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

    @Bean
    @ConditionalOnMissingBean(name = SecurityConstants.SMS_VALIDATE_CODE_PROCESSOR)
    public ValidateCodeProcessor smsValidateCodeProcessor() {
        return new SmsValidateCodeProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(name = SecurityConstants.IMAGE_VALIDATE_CODE_PROCESSOR)
    public ValidateCodeProcessor imageValidateCodeProcessor() {
        return new ImageValidateCodeProcessor();
    }
}
