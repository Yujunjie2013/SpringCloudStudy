package org.junjie.security.core.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialProperties {
    private QQProperties qq = new QQProperties();
    private WeixinProperties weixin = new WeixinProperties();
    private String filterProcessesUrl = "/auth";//默认的还是auth
}
