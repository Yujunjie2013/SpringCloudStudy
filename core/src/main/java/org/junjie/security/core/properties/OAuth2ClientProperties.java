package org.junjie.security.core.properties;

import lombok.Data;

@Data
public class OAuth2ClientProperties {
    private String clientId;
    private String clientSecret;
    //令牌有效期，如果是0表示不会过期
    private int accessTokenValiditySeconds=7200;
}
