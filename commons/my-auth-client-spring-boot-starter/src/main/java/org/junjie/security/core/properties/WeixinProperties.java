package org.junjie.security.core.properties;

import lombok.Data;

@Data
public class WeixinProperties {
    private String appId;
    private String appSecret;
    private String providerId = "weixin";
}
