package org.junjie.security.core.validate.code;


import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证码异常
 */
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = -3827140217384L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
