package com.pengguli.orderservice.service.impl;

import com.pengguli.commonutils.order.CourseWebVoOrder;
import com.pengguli.commonutils.order.UcenterMemberOrder;
import com.pengguli.orderservice.client.EduCourseClient;
import com.pengguli.orderservice.client.UcenterMemberClient;
import com.pengguli.orderservice.pojo.Order;
import com.pengguli.orderservice.mapper.OrderMapper;
import com.pengguli.orderservice.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pengguli.orderservice.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author peng
 * @since 2023-01-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UcenterMemberClient ucenterMemberClient;
    @Autowired
    private EduCourseClient eduCourseClient;

    @Override
    public String createdOrders(String courseId, String memberIdByJwtToken) {
        System.out.println(courseId);
        // 通过远程调用，根据用户id获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterMemberClient.getUserInfoOrder(memberIdByJwtToken);
        System.out.println(userInfoOrder.toString());
        // 通过远程调用，根据课程id获取课程信息
        CourseWebVoOrder courseInfo = eduCourseClient.getCourseInfo(courseId);
        System.out.println(courseInfo);
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfo.getTitle());

        order.setCourseCover(courseInfo.getCover());
        order.setTeacherName(courseInfo.getTeacherName());
        order.setTotalFee(courseInfo.getPrice());
        order.setMemberId(memberIdByJwtToken);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);
        System.out.println(order);
        return order.getOrderNo();
    }

}
