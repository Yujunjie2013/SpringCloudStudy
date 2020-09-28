package com.example.authserver.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * 登录成功处理器
 * SavedRequestAwareAuthenticationSuccessHandler是Spring默认的登录成功处理器
 */
@Component("appAuthenticationSuccessHandler")
public class AppAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功");
        super.onAuthenticationSuccess(request,response,authentication);

//        String header = request.getHeader("Authorization");
//        if (header == null || !header.startsWith("Basic ")) {
//            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
//        }
//        String[] strings = extractAndDecodeHeader(header, request);
//        String clientId = strings[0];
//        String clientSecret = strings[1];
//        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//
//        if (clientDetails == null) {
//            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
//        } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
//            throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientSecret);
//        }
//
//        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");
//        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
//
//        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
//        OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
//
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(accessToken));
    }

    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws UnsupportedEncodingException {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decodeBase64(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Faild to decode basic authentication token");
        }
        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }
}
