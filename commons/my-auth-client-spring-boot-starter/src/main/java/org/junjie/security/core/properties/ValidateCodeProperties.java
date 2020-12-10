package org.junjie.security.core.properties;

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
    /**
     * 验证码存储总有效期，默认10分钟，600秒
     */
    private int totalExpireIn = 600;
}
