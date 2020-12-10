package com.example.authserver.config;

public interface AuthConstants {
    /**
     * 手机号密码登录模式GRANT_TYPE
     */
    String MOBILE_PASSWORD_GRANT_TYPE = "mobile_password";
    /**
     * 微信授权登录
     */
    String OPENID_GRANT_TYPE = "openId";
    /**
     * 图像验证码模式
     */
    String PASSWORD_CODE_GRANT_TYPE = "password_code";

    /**
     * 验证码类型
     */
    interface TYPE {
        /**
         * 短信
         */
        String SMS = "sms";
        /**
         * 验证码
         */
        String IMAGE = "image";
    }


}
