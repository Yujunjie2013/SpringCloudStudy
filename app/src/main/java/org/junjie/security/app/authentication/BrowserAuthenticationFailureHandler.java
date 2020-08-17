package org.junjie.security.app.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junjie.security.core.properties.LoginType;
import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.support.SimpleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器，SimpleUrlAuthenticationFailureHandler是Spring默认的失败处理器
 */
@Component("browserAuthenticationFailureHandler")
public class BrowserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * @param request
     * @param response
     * @param exception 包含了在认证过程中失败的错误原因
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.info("发生异常:" + exception.getMessage());
        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
