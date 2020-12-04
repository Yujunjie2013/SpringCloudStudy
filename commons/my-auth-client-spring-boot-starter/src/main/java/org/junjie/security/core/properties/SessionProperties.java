package org.junjie.security.core.properties;

import lombok.Data;

@Data
public class SessionProperties {
    /**
     * 同一个用户在系统中的最大session数，默认1
     */
    private int maximumSession = 1;
    /**
     * 同一用户达到最大session时是否阻止新的登录请求，默认为false，不阻止，新的登录会将老的登录失效掉
     */
    private boolean maxSessionPreventsLogin;
    /**
     * session失效时的跳转地址
     */
    private String sessionInvalidUrl = SecurityConstants.DEFAULT_SESSION_INVALID_URL;
}
