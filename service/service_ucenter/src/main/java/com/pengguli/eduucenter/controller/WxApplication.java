package com.pengguli.eduucenter.controller;

import com.google.gson.Gson;
import com.pengguli.commonutils.JwtUtils;
import com.pengguli.eduucenter.pojo.UcenterMember;
import com.pengguli.eduucenter.service.UcenterMemberService;
import com.pengguli.eduucenter.utils.ConstantWxUtils;
import com.pengguli.eduucenter.utils.HttpClientUtils;
import com.pengguli.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
//@Controller
@RequestMapping("/eduucenter/wx")
public class WxApplication {

    @Autowired
    private UcenterMemberService memberService;

    @GetMapping("login")
    public String wXLogin(){
        // 微信开放平台授权baseUrl，%s相当于一个占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String redirectUri;
        try {
            redirectUri = URLEncoder.encode(ConstantWxUtils.WX_OPEN_REDIRECT_URL,"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String url = String.format(baseUrl, ConstantWxUtils.WX_OPEN_APP_ID,redirectUri, "pengguli");

        return "redirect:" + url;
    }

    // 获取登陆人信息
    @GetMapping("callback")
    public String callback(String code,String state){
        // 1、获取code值，临时票据，类似于验证码

        // 2、拿着code请求，微信固定地址，得到两个值 accsess_token和openid
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        // 拼接上边地址的三个参数
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_APP_SECRET,
                code);
        // 请求这个地址，得到两个返回值accsess_token和openid
        String accessTokenInfo = null;
        try {
            accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            // 通过gson，解析accessTokenInfo，得到accsess_token和openid
            Gson gson = new Gson();
            HashMap hashMap = gson.fromJson(accessTokenInfo, HashMap.class);
            String accessToken = (String) hashMap.get("access_token");
            String openid = (String) hashMap.get("openid");

            // 根据openid判断数据库是否有数据，有添加，没有注册
            UcenterMember member =  memberService.getOpenIdWx(openid);
            if (member == null){
                // 3、拿着accsess_token和openid再去请求固定地址，获取扫码人的信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfo = String.format(baseUserInfoUrl, accessToken, openid);
                String resultUserInfo = HttpClientUtils.get(userInfo);
                //解析json
                HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
                String nickname = (String)mapUserInfo.get("nickname");
                String headimgurl = (String)mapUserInfo.get("headimgurl");

                // 注册
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            // 返回首页面
            return "redirect:http://localhost:3000?token="+token;
        } catch (Exception e) {
            throw new GuliException(20001,"登录失败");
        }
    }
}
/**
 * {
 * "access_token":"64_Lgx00NnwriuV76o_4t5qk7hr1OqFYan3Vw-WBizeOTNv-PBqWimj4OSOY0rdF3OSR5xKBq3hxVm10OKDCwZ_xmva7Q_NS4yj_0wYdy6nIgM",
 * "expires_in":7200,
 * "refresh_token":"64_gLBLVBUsaQyRLB-TrEUSAC8vJDADL8DOpR4IGUH9GmHtZ0i2SrdoYh8AiKcmgbN4KscxBYHraE5EiAMJTTT3WtN1MEi9WE6uejQQpevYbDs",
 * "openid":"o3_SC5wXFE8EGmdDUW2Xm9P_e1Fs",
 * "scope":"snsapi_login",
 * "unionid":"oWgGz1MIPr3NpB14LJVftFzZIUDA"
 * }
 */