package org.junjie.security.core.properties;

import lombok.Data;

@Data
public class BrowserProperties {
    /**
     * 默认注册页
     */
    private String signUpUrl = "/imooc-signUp.html";
    /**
     * 登录页面，如果用户没有配置，则使用默认的登录页面/imooc-sign.html
     */
    private String loginPage = SecurityConstants.DEFAULT_SIGN_IN_PAGE_URL;
    /**
     * 登录页面,成功后是返回Json还是重定向
     */
    private LoginType loginType = LoginType.JSON;
    /**
     * 记住我有效时长,默认1小时
     */
    private int rememberMeSeconds = 3600;
    /**
     * 退出成功时跳转的url，如果配置了，则跳到指定的url，如果没配置，则返回json数据。
     */
    private String signOutUrl;
    /**
     * session管理配置项
     */
    private SessionProperties session = new SessionProperties();
}
