package org.junjie.security.app;

import org.junjie.security.app.social.impl.AppSignUpUtils;
import org.junjie.security.core.support.SocialUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AppSecurityController {

    @Autowired
    private AppSignUpUtils appSignUpUtils;
    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 如果是app授权登录，但是这个用户是第一次登录在系统中不存在，这时会被转发到这里
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/social/signUp")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request, HttpServletResponse response) {
        SocialUserInfo userInfo = new SocialUserInfo();
        Connection<?> connectionFromSession = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request, response));
        userInfo.setProviderId(connectionFromSession.getKey().getProviderId());
        userInfo.setProviderUserId(connectionFromSession.getKey().getProviderUserId());
        userInfo.setNickName(connectionFromSession.getDisplayName());
        userInfo.setHeadImg(connectionFromSession.getImageUrl());
        appSignUpUtils.saveConnectionData(new ServletWebRequest(request, response), connectionFromSession.createData());
        return userInfo;
    }
}
