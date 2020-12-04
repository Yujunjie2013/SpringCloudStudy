package org.junjie.security.core.social.weixin.connect;

import org.junjie.security.core.social.weixin.api.Weixin;
import org.junjie.security.core.social.weixin.api.WeixinUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

public class WeixinAdapter implements ApiAdapter<Weixin> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String openId;

    /**
     * openId是通过构造传过来的，因为每个用户都不一样，所以这里WeixinAdapter意味着会有多个实例
     * @param openId
     */
    public WeixinAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(Weixin api) {
        return true;
    }

    @Override
    public void setConnectionValues(Weixin api, ConnectionValues values) {
        logger.info("调用微信api获取信息setConnectionValues");
        WeixinUserInfo profile = api.getUserInfo(openId);
        values.setProviderUserId(profile.getOpenid());
        values.setDisplayName(profile.getNickname());
        values.setImageUrl(profile.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(Weixin api) {
        return null;
    }

    @Override
    public void updateStatus(Weixin api, String message) {

    }
}
