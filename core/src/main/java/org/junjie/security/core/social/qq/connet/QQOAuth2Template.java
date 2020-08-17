package org.junjie.security.core.social.qq.connet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * 个性化OAuth2Template，官方默认提供的不支持text/html;但是QQ返回的是这种
 * 同时在创建AccessGrant时，官方默认是从json中取，但是qq返回的是一个字符串使用默认的会报错，所以我们自定义解析并生成AccessGrant
 */
public class QQOAuth2Template extends OAuth2Template {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        //这里设置为true,在请求参数的时候才会带上client_id,和client_secret,在父类的exchangeForAccess方法中添加参数
        setUseParametersForClientAuthentication(true);
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        //默认的RestTemplate不支持text/html;所以需要重新添加一个支持text/html;的转换器
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    /**
     * 这个方法的父类期望返回的是一个json数据，但是qq返回的是一个字符串，所以我们需要重写该方法，否则解析会有问题
     *
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
//        super.postForAccessGrant(accessTokenUrl, parameters);
        String s = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        logger.info("获取accessToken的响应:" + s);
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(s, "&");
        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");
        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
    }
}
