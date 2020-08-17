package org.junjie.security.browser.session;

import org.junjie.security.core.properties.SecurityProperties;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的session失效处理策略
 */
public class DefaultInvalidSessionStrategy extends AbstractSessionStrategy implements InvalidSessionStrategy {
    public DefaultInvalidSessionStrategy(SecurityProperties securityPropertie) {
        super(securityPropertie);
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        onSessionInvalid(request, response);
    }
}
