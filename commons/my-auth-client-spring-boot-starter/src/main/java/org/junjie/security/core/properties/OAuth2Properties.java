package org.junjie.security.core.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2Properties {
    private OAuth2ClientProperties[] clients = {};
    private String jwtSigningKey = "org_junjie";
    private RenewProperties renew = new RenewProperties();
}
