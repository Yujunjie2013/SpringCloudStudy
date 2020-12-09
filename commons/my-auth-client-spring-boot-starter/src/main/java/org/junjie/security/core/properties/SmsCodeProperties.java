package org.junjie.security.core.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsCodeProperties {
    private int length = 6;//验证码长度
    private int expireIn = 60;//验证码默认有效期
    private String url;////拦截的地址
}
