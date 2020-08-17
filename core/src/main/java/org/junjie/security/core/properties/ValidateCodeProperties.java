package org.junjie.security.core.properties;

import lombok.Data;

@Data
public class ValidateCodeProperties {
    /**
     * 图形验证码
     */
    private ImageCodeProperties image = new ImageCodeProperties();
    /**
     * 短信验证码
     */
    private SmsCodeProperties sms = new SmsCodeProperties();
}
