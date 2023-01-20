package com.pengguli.orderservice.controller;


import com.pengguli.commonutils.R;
import com.pengguli.orderservice.service.PayLogService;
import com.pengguli.orderservice.service.impl.PayLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author peng
 * @since 2023-01-09
 */
@RestController
//@CrossOrigin
@RequestMapping("/orderservice/paylog")
public class PayLogController {

    @Autowired
    private PayLogServiceImpl payLogService;

    // 1、生成微信支付二维码
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        Map map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    // 2、根据id查询订单状态并修改
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        // 查询订单状态
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        if (map == null){
            return R.error().message("支付出错");
        }
        // 更新订单状态
        if (map.get("trade_state").equals("SUCCESS")){
            payLogService.updateOrderStatus(map);
            return R.ok();
        }
        return R.ok().code(25000).message("支付中");
    }
}