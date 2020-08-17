package org.junjie.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "org.junjie")
@Data
public class SecurityProperties {
    private BrowserProperties browser = new BrowserProperties();
    /**
     * 验证码（包含短信验证，图形验证）
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();

    private SocialProperties social = new SocialProperties();

    private OAuth2Properties oauth2 = new OAuth2Properties();
}
