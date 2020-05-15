package com.junjie.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFilter extends ZuulFilter {
    /**
     * "pre" for pre-routing filtering,
     * "route" for routing to an origin,
     * "post" for post-routing filters,
     * "error" for error handling.
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器执行顺序，值越小越靠前
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 是否使用当前过虐器
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器中的业务逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("执行了过滤器");
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        //请求获取access-token参数
        String token = request.getParameter("access-token");
        if (StringUtils.isEmpty(token)) {
            //如果为空请求失败
            HttpServletResponse response = currentContext.getResponse();
            response.setContentType("text/html;charset=UTF-8");
            currentContext.setResponseBody("未认证");
            currentContext.setSendZuulResponse(false);//拦截请求
            currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());//未认证
        }
        return null;
    }
}
