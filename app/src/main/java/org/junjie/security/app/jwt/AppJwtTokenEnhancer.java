package org.junjie.security.app.jwt;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class AppJwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        //增加自定义信息
        Map<String, Object> info = new HashMap<>();
        info.put("company", "imooc");//发出去的令牌中增加一个自定义字段
        DefaultOAuth2AccessToken auth2AccessToken = (DefaultOAuth2AccessToken) accessToken;
        auth2AccessToken.setAdditionalInformation(info);
        return auth2AccessToken;
    }
}
