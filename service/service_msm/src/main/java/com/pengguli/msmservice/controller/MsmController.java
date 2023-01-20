package com.pengguli.msmservice.controller;

import com.pengguli.commonutils.R;
import com.pengguli.msmservice.service.MsmService;
import com.pengguli.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
//@CrossOrigin
@RequestMapping("/edumsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        // 使用Redis解决验证么，有效时间问题
        String code = redisTemplate.opsForValue().get(phone);
        // 不为空，直接返回
        if (!StringUtils.isEmpty(code)) return R.ok();

        // 生成随机值，用于发送验证码的值,四位
        code = RandomUtil.getFourBitRandom();
        // 将值放入Map集合，因为这个验证码传送给阿里云，必须是JSON格式
        Map<String,Object> param = new HashMap<>();
        param.put("code",code);
        // 调用发送短信的方法
        boolean flag = msmService.send(param,phone);
        if (flag){
            // 发送成功，把验证码存到redis中，设置短信有效期
            redisTemplate.opsForValue().set(phone,code,1, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("短信发送失败");
        }
    }

}
