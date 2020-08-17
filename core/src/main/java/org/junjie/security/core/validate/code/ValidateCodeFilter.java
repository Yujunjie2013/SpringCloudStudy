package org.junjie.security.core.validate.code;

import org.apache.commons.lang.StringUtils;
import org.junjie.security.core.properties.SecurityConstants;
import org.junjie.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    //失败处理器
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    //系统中的校验码处理器
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;
    //系统配置
    @Autowired
    private SecurityProperties securityProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    //存放需要进行验证码验证的接口路径
    private final Map<String, ValidateCodeType> urlMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        //账号密码登录默认需要使用图形验证码
        addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);

        urlMap.put(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        //手机短信登录
        addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
    }

    private void addUrlToMap(String url, ValidateCodeType codeType) {
        if (StringUtils.isNotBlank(url)) {
            String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(url, ",");
            for (String configUrl : configUrls) {
                urlMap.put(configUrl, codeType);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        ValidateCodeType type = getValidateCodeType(httpServletRequest);

        //如果为空则不需要校验
        if (type != null) {
            logger.info("校验请求(" + httpServletRequest.getRequestURI() + ")中的验证码类型:" + type);
            try {
                validateCodeProcessorHolder.findValidateCodeProcessor(type)
                        .validate(new ServletWebRequest(httpServletRequest, httpServletResponse));
                logger.info("验证码校验通过");
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     *
     * @param request 请求参数
     * @return 校验码类型
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;
        //就算配置了url，如果是get请求也不会校验
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                }
            }
        }
        return result;
    }
}
