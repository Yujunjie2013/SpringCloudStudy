package org.junjie.security.core.social.qq.api;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String appId;
    private String openId;
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    private static final String URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
    private Gson gson = new Gson();

    public QQImpl(String accessToken, String appId) {
        //父类1个参数的构造方法是AUTHORIZATION_HEADER，将token放在请求头中，但是qq的是放在请求参数中的，所以这里要用
        //2个参数的构造，指定使用参数来传递
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;
        String url = String.format(URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);
        logger.info("获取到openId:" + result);
        openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    @Override
    public QQUserInfo getUserInfo() {
        String url = String.format(URL_GET_USER_INFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);
        logger.info("getUserInfo结果:" + result);
        QQUserInfo qqUserInfo = gson.fromJson(result, QQUserInfo.class);
        qqUserInfo.setOpenId(openId);
        return qqUserInfo;
    }
}
