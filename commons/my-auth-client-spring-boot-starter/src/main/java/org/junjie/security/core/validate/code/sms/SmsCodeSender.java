package org.junjie.security.core.validate.code.sms;

/**
 * 验证码发送接口
 */
public interface SmsCodeSender {
    void send(String mobile, String code);
}
