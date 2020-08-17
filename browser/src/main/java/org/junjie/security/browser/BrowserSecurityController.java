package org.junjie.security.browser;

import org.apache.commons.lang.StringUtils;
import org.junjie.security.core.support.SimpleResponse;
import org.junjie.security.core.support.SocialUserInfo;
import org.junjie.security.core.properties.SecurityConstants;
import org.junjie.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowserSecurityController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 但需要身份认证时，跳转到这里
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContextHolder.getContext().getAuthentication();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String target = savedRequest.getRedirectUrl();
            logger.info("引发跳转的URL:" + target);
            if (StringUtils.endsWithIgnoreCase(target, ".html")) {
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
                logger.info("返回一个null");
                return null;
            }
        }
        response.setContentType("application/json;charset=UTF-8");
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
    }


    @GetMapping("/social/user")
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request, HttpServletResponse response) {
        SocialUserInfo userInfo = new SocialUserInfo();
        Connection<?> connectionFromSession = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request, response));
        userInfo.setProviderId(connectionFromSession.getKey().getProviderId());
        userInfo.setProviderUserId(connectionFromSession.getKey().getProviderUserId());
        userInfo.setNickName(connectionFromSession.getDisplayName());
        userInfo.setHeadImg(connectionFromSession.getImageUrl());
        return userInfo;
    }

    /**
     * session 失效简单处理
     * @return
     */
    @GetMapping("/session/invalid")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse sessionInvalid() {
        String message = "session 失效";
        return new SimpleResponse(message);
    }

}
