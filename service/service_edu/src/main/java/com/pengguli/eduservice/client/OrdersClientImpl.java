package com.pengguli.eduservice.client;

import org.springframework.stereotype.Component;

@Component
public class OrdersClientImpl implements OrdersClient{
    @Override
    public boolean getOrderStatus(String courseId, String userId) {
        return false;
    }
}
