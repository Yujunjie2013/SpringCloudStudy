package org.junjie.security.browser.logout;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.junjie.security.core.support.SimpleResponse;
import org.junjie.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private SecurityProperties securityProperties;
    private Gson gson = new Gson();

    public DefaultLogoutSuccessHandler(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("退出成功");
        String signOutUrl = securityProperties.getBrowser().getSignOutUrl();
        if (StringUtils.isBlank(signOutUrl)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(gson.toJson(new SimpleResponse("退出成功")));
        } else {
            response.sendRedirect(signOutUrl);
        }
    }
}
