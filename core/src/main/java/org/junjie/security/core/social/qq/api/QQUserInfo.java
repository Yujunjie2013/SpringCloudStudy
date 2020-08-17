package org.junjie.security.core.social.qq.api;

import lombok.Data;

@Data
public class QQUserInfo {

    /**
     * ret : 0
     * msg :
     * nickname : Peter
     * figureurl : http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/30
     * figureurl_1 : http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/50
     * figureurl_2 : http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/100
     * figureurl_qq_1 : http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/40
     * figureurl_qq_2 : http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/100
     * gender : 男
     * is_yellow_vip : 1
     * vip : 1
     * yellow_vip_level : 7
     * level : 7
     * is_yellow_year_vip : 1
     */

    private int ret;
    private String msg;
    private String nickname;
    private String figureurl;
    private String figureurl_1;
    private String figureurl_2;
    private String figureurl_qq_1;
    private String figureurl_qq_2;
    private String gender;
    private String is_yellow_vip;
    private String vip;
    private String yellow_vip_level;
    private String level;
    private String is_yellow_year_vip;
    private String openId;

}
