package org.junjie.security.core.validate.code;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ValidateCode implements Serializable {

    private static final long serialVersionUID = 1588203828504660925L;
    private String code;
    private LocalDateTime expireTime;

    /**
     * 判断验证码是否过时
     *
     * @return
     */
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }
}
