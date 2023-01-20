package com.pengguli.orderservice.service;

import com.pengguli.orderservice.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author peng
 * @since 2023-01-09
 */
public interface OrderService extends IService<Order> {

    // 1、生成订单
    public String createdOrders(String courseId, String memberIdByJwtToken);

}
