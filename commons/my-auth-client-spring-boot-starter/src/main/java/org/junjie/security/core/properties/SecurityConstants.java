/**
 *
 */
package org.junjie.security.core.properties;

/**
 * 常量配置类
 *
 */
public interface SecurityConstants {

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";
    /**
     * 当请求需要身份认证时，默认跳转的url
     *
     * 代码 BrowserSecurityController
     */
    String DEFAULT_UNAUTHENTICATION_URL = "/authentication/require";
    /**
     * 默认的用户名密码登录请求处理url
     */
    String DEFAULT_SIGN_IN_PROCESSING_URL_FORM = "/authentication/form";
    /**
     * 默认的手机验证码登录请求处理url
     */
    String DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE = "/authentication/mobile";
    /**
     * 默认的OPENID登录请求处理url
     */
    String DEFAULT_SIGN_IN_PROCESSING_URL_OPENID = "/authentication/openid";
    /**
     * 默认登录页面
     *
     */
    String DEFAULT_SIGN_IN_PAGE_URL = "/imooc-sign.html";
    /**
     * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";
    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";
    /**
     * openid参数名
     */
    String DEFAULT_PARAMETER_NAME_OPENID = "openId";
    /**
     * providerId参数名
     */
    String DEFAULT_PARAMETER_NAME_PROVIDERID = "providerId";
    /**
     * session失效默认的跳转地址
     */
    String DEFAULT_SESSION_INVALID_URL = "/imooc-session-invalid.html";
    /**
     * 获取第三方用户信息的url
     */
    String DEFAULT_SOCIAL_USER_INFO_URL = "/social/user";

    /**
     * 验证码生成器bean ID,自定义图形验证码生成器时需要使用该id
     */
    String IMAGE_CODE_GENERATOR_ID = "imageCodeGenerator";
    /**
     * 短信验证码生成器bean ID，自定义短信验证码生成器时需要使用该id
     */
    String SMS_CODE_GENERATOR_ID = "smsCodeGenerator";
    /**
     *验证码生成器(包含短信、图片)，smsCodeGenerator/imageCodeGenerator
     */
    String CODE_GENERATOR_ID = "CodeGenerator";
    /**
     *图形验证码处理器
     */
    String IMAGE_VALIDATE_CODE_PROCESSOR = "imageValidateCodeProcessor";
    /**
     * 短信验证码处理器
     */
    String SMS_VALIDATE_CODE_PROCESSOR = "smsValidateCodeProcessor";
}
