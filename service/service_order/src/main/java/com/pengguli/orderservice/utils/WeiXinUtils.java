package com.pengguli.orderservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WeiXinUtils implements InitializingBean {

    @Value("${weixin.play.appid}")
    private String oppenId;

    @Value("${weixin.play.partner}")
    private String partner;

    @Value("${weixin.play.partnerkey}")
    private String partnerkey;

    @Value("${weixin.play.notifyurl}")
    private String notifyurl;

    public static String WEI_XIN_OPPENID;
    public static String PARTNER;
    public static String PARTNER_KEY;
    public static String NOTIFY_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        WEI_XIN_OPPENID = oppenId;
        PARTNER = partner;
        PARTNER_KEY = partnerkey;
        NOTIFY_URL = notifyurl;
    }
}
