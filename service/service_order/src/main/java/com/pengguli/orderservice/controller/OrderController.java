package com.pengguli.orderservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pengguli.commonutils.JwtUtils;
import com.pengguli.commonutils.R;
import com.pengguli.orderservice.pojo.Order;
import com.pengguli.orderservice.service.OrderService;
import com.pengguli.orderservice.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author peng
 * @since 2023-01-09
 */
@RestController
//@CrossOrigin
@RequestMapping("/orderservice/order")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    // 1、生成订单
    @PostMapping("saveOrders/{courseId}")
    public R saveOrders(@PathVariable String courseId, HttpServletRequest request){
        String orderNo =orderService.createdOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderNo",orderNo);
    }

    // 2、根据id查询订单
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("order",order );
    }

    // 3、根据课程id和用户id查询课程状态
    @GetMapping("isBuyCourse/{courseId}/{userId}")
    public boolean getOrderStatus(@PathVariable String courseId,@PathVariable String userId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId).eq("member_id",userId).eq("status",1);
        int count = orderService.count(wrapper);
        // 已经支付
        if (count > 0) return true;
        return false;
    }
}

