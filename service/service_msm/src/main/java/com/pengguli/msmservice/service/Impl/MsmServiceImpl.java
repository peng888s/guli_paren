package com.pengguli.msmservice.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.StringUtils;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.pengguli.msmservice.service.MsmService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    // 发送短信的方法
    @Override
    public boolean send(Map<String, Object> param, String phone) {
        // 判断手机号是否为空
        if(StringUtils.isEmpty(phone)) return false;

        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI5tM1q5FxS5cFoaGhTatq", "g1oLrhTX2O6TIftPEZ7jGlepuIoWDm");
        IAcsClient client = new DefaultAcsClient(profile);

        // 固定值，不能变
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        // 设置发送相关参数，注意key的值不能变，固定的
        request.putQueryParameter("PhoneNumbers",phone); // 手机号
        request.putQueryParameter("SignName","阿里云短信测试"); // 申请的签名名称
        request.putQueryParameter("TemplateCode","SMS_154950909"); // 模板Code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); // 发送的验证码，只能是JSON格式

        // 最终发送
        try {
            CommonResponse commonResponse = client.getCommonResponse(request);
            boolean success = commonResponse.getHttpResponse().isSuccess();
            return success;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}
