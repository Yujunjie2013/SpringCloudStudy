package org.junjie.security.core.social.weixin.api;

public interface Weixin {
    /**
     * 获取微信用户信息
     * @param openId 微信用户ID
     * @return
     */
    WeixinUserInfo getUserInfo(String openId);
}
