package com.pengguli.msmservice.service;

import java.util.Map;

public interface MsmService {
    // 调用发送短信的方法
    boolean send(Map<String, Object> param, String phone);
}
