package org.junjie.security.core.social.qq.connet;

import org.junjie.security.core.social.qq.api.QQ;
import org.junjie.security.core.social.qq.api.QQImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String appId;

    private static final String URL_AUTHORIZZE = "https://graph.qq.com/oauth2.0/authorize";
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     * Create a new {@link OAuth2ServiceProvider}.
     *
     * @param oauth2Operations the OAuth2Operations template for conducting the OAuth 2 flow with the provider.
     */
    public QQServiceProvider(String appId, String appSecret) {
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZZE, URL_ACCESS_TOKEN));
        this.appId = appId;
        logger.info("QQServiceProvider开始实例化----appId:" + appId);
    }

    @Override
    public QQ getApi(String accessToken) {
        logger.info("getApi获取传入token:" + accessToken);
        return new QQImpl(accessToken, appId);
    }
}
