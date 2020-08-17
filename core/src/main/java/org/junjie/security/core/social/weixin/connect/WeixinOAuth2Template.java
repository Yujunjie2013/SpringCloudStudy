package org.junjie.security.core.social.weixin.connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WeixinOAuth2Template extends OAuth2Template {
    private final String clientId;

    private final String clientSecret;

    private final String accessTokenUrl;

    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public WeixinOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
    }


    /**
     * 微信不是标准的OAuth2流程，入参会有变化，父类的不满足，所以这里要重写
     * @param authorizationCode
     * @param redirectUri
     * @param parameters
     * @return
     */
    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri,
                                         MultiValueMap<String, String> parameters) {
        //构建获取token请求url
        StringBuilder accessTokenRequestUrl = new StringBuilder(accessTokenUrl);
        accessTokenRequestUrl.append("?appid=").append(clientId)
                .append("&secret=").append(clientSecret)
                .append("&code=").append(authorizationCode)
                .append("&grant_type=authorization_code")
                .append("&redirect_uri=").append(redirectUri);
        //获取token
        return getAccessToken(accessTokenRequestUrl);
    }

    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        //构造刷新token请求url
        StringBuilder refreshTokenUrl = new StringBuilder(REFRESH_TOKEN_URL);
        refreshTokenUrl.append("?appid=").append(clientId)
                .append("&grant_type=refresh_token")
                .append("&refresh_token=").append(refreshToken);

        return getAccessToken(refreshTokenUrl);
    }

    @SuppressWarnings("unchecked")
    private AccessGrant getAccessToken(StringBuilder accessTokenRequestUrl) {

        logger.info("获取access_token, 请求URL: " + accessTokenRequestUrl.toString());

        String response = getRestTemplate().getForObject(accessTokenRequestUrl.toString(), String.class);

        logger.info("获取access_token, 响应内容: " + response);
        Map<String, Object> result = null;
        try {
            result = new ObjectMapper().readValue(response, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回错误码时直接返回空
        if (StringUtils.isNotBlank(MapUtils.getString(result, "errcode"))) {
            String errcode = MapUtils.getString(result, "errcode");
            String errmsg = MapUtils.getString(result, "errmsg");
            throw new RuntimeException("获取access token失败, errcode:" + errcode + ", errmsg:" + errmsg);
        }

        WeixinAccessGrant accessToken = new WeixinAccessGrant(
                MapUtils.getString(result, "access_token"),
                MapUtils.getString(result, "scope"),
                MapUtils.getString(result, "refresh_token"),
                MapUtils.getLong(result, "expires_in"));

        accessToken.setOpenId(MapUtils.getString(result, "openid"));

        return accessToken;
    }

    /**
     * 构建获取授权码的请求。也就是引导用户跳转到微信的地址。
     */
    public String buildAuthenticateUrl(OAuth2Parameters parameters) {
        String url = super.buildAuthenticateUrl(parameters);
        url = url + "&appid=" + clientId + "&scope=snsapi_login";
//      https://open.weixin.qq.com/connect/qrconnect?client_id=wxd99431bbff8305a0&response_type=code&redirect_uri=http%3A%2F%2Fwww.pinzhi365.com%2FqqLogin%2Fweixin&state=9afc03d6-87ad-424a-b2be-29d59f1c4b1f&appid=wxd99431bbff8305a0&scope=snsapi_login
        //因为微信不是标准的OAuth2，他的入参不是client_id，而是需要替换成appid,这里其实可以直接使用replace将client_id替换成appid
        //为了方便就直接在后面拼上了
        logger.info("认证Url:" + url);
        return url;
    }

    public String buildAuthorizeUrl(OAuth2Parameters parameters) {
        return buildAuthenticateUrl(parameters);
    }

    /**
     * 微信返回的contentType是html/text，添加相应的HttpMessageConverter来处理。
     */
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
