package org.junjie.security.core.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeixinProperties {
    private String appId;
    private String appSecret;
    private String providerId = "weixin";
}
