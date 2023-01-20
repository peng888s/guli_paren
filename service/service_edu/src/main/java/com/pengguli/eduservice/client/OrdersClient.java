package com.pengguli.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order",fallback = OrdersClientImpl.class)
public interface OrdersClient {
    // 判断课程是否已经购买
    @GetMapping("/orderservice/order/isBuyCourse/{courseId}/{userId}")
    public boolean getOrderStatus(@PathVariable("courseId") String courseId, @PathVariable("userId") String userId);
}
