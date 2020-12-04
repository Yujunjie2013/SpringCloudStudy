package org.junjie.security.core.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageCodeProperties extends SmsCodeProperties {
    private int width = 100; // 验证码图片宽度
    private int height = 36; // 验证码图片长度

    public ImageCodeProperties() {
        setLength(4);//设置图形验证码的默认长度为4
    }
}
