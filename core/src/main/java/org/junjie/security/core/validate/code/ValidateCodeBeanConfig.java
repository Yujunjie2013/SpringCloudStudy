package org.junjie.security.core.validate.code;

import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.validate.code.image.ImageCodeGenerator;
import org.junjie.security.core.validate.code.sms.DefaultSmsCodeSender;
import org.junjie.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码bean配置类
 */
@Configuration
public class ValidateCodeBeanConfig {
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 验证码生成器，如果用户没有定义imageCodeGenerator时
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
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
}
