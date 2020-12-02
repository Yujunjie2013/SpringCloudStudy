package com.example.authserver.granter;

import org.junjie.security.core.validate.code.ValidateCodeProcessorHolder;
import org.junjie.security.core.validate.code.ValidateCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * password添加图像验证码授权模式
 *
 * @author zlt
 * @date 2020/7/11
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
public class PwdImgCodeGranter extends ResourceOwnerPasswordTokenGranter {
    private static final String GRANT_TYPE = "password_code";

    //系统中的校验码处理器
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    public PwdImgCodeGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices
            , ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, ValidateCodeProcessorHolder validateCodeProcessorHolder) {
        super(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.validateCodeProcessorHolder = validateCodeProcessorHolder;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String deviceId = parameters.get("deviceId");
        String validCode = parameters.get("validCode");
        //校验图形验证码
//        validateCodeService.validate(deviceId, validCode);
        validateCodeProcessorHolder.findValidateCodeProcessor("image").validate(deviceId, validCode, ValidateCodeType.IMAGE);
        return super.getOAuth2Authentication(client, tokenRequest);
    }
}
