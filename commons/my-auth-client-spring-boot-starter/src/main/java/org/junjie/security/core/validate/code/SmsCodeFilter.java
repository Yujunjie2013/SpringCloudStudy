//package org.junjie.security.core.validate.code;
//
//import org.apache.commons.lang.StringUtils;
//import org.junjie.security.core.properties.SecurityProperties;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.social.connect.web.HttpSessionSessionStrategy;
//import org.springframework.social.connect.web.SessionStrategy;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.bind.ServletRequestBindingException;
//import org.springframework.web.bind.ServletRequestUtils;
//import org.springframework.web.context.request.ServletWebRequest;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//将短信验证码和图片验证码抽取到同一个处理器中进行处理
////@Component
//public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean {
//    //存放需要进行验证码验证的接口路径
//    private Set<String> urls = new HashSet<>();
//    @Autowired
//    private AuthenticationFailureHandler failureHandler;
//    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
//    private SecurityProperties securityProperties;
//    private AntPathMatcher pathMatcher = new AntPathMatcher();
//
//    @Override
//    public void afterPropertiesSet() throws ServletException {
//        super.afterPropertiesSet();
//        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getSms().getUrl(), ",");
//        if (configUrls != null && configUrls.length != 0) {
//            for (String configUrl : configUrls) {
//                urls.add(configUrl);
//            }
//        }
//        //默认将登陆也添加进去
//        urls.add("/authentication/mobile");
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        boolean check = false;
//        for (String url : urls) {
//            if (pathMatcher.match(url, httpServletRequest.getRequestURI())) {
//                check = true;
//                break;
//            }
//        }
//        //如果是登录请求校验
//        if (check) {
//            try {
//                validate(new ServletWebRequest(httpServletRequest));
//            } catch (ValidateCodeException e) {
//                failureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
//                return;
//            }
//        }
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//
//    private void validate(ServletWebRequest servletWebRequest) throws ValidateCodeException, ServletRequestBindingException {
//        ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(servletWebRequest, ValidateCodeProcessor.SESSION_KEY_PREFIX + "SMS");
//        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "smsCode");
//        if (StringUtils.isBlank(codeInRequest)) {
//            throw new ValidateCodeException("验证码的值不能为空");
//        }
//        if (codeInSession == null) {
//            throw new ValidateCodeException("验证码不存在");
//        }
//        if (codeInSession.isExpried()) {
//            throw new ValidateCodeException("验证码已过期");
//        }
//        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
//            throw new ValidateCodeException("验证码不匹配");
//        }
//        sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeProcessor.SESSION_KEY_PREFIX + "SMS");
//    }
//
//    public AuthenticationFailureHandler getFailureHandler() {
//        return failureHandler;
//    }
//
//    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
//        this.failureHandler = failureHandler;
//    }
//
//    public SecurityProperties getSecurityProperties() {
//        return securityProperties;
//    }
//
//    public void setSecurityProperties(SecurityProperties securityProperties) {
//        this.securityProperties = securityProperties;
//    }
//}
