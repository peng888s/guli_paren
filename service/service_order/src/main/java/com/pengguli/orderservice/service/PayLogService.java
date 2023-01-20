package com.pengguli.orderservice.service;

import com.pengguli.orderservice.pojo.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author peng
 * @since 2023-01-09
 */
public interface PayLogService extends IService<PayLog> {

    // 1、生成微信支付二维码
    Map createNative(String orderNo);

    // 更新订单状态
    void updateOrderStatus(Map<String, String> map);

    // 查询订单状态
    Map<String, String> queryPayStatus(String orderNo);
}
