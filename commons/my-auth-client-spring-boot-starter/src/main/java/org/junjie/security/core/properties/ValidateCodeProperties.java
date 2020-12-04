package org.junjie.security.core.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
