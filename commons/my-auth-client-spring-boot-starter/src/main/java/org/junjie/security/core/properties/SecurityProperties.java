package org.junjie.security.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "org.junjie")
@Getter
@Setter
public class SecurityProperties {
    /**
     * 浏览器
     */
    private BrowserProperties browser = new BrowserProperties();
    /**
     * 验证码（包含短信验证，图形验证）
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();
    /**
     * 社交登录相关
     */
    private SocialProperties social = new SocialProperties();
    /**
     * 认证相关
     */
    private OAuth2Properties oauth2 = new OAuth2Properties();
}
