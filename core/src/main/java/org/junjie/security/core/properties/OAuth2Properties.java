package org.junjie.security.core.properties;

import lombok.Data;

@Data
public class OAuth2Properties {
    private OAuth2ClientProperties[] clients = {};
    private String jwtSigningKey = "imooc";
}
