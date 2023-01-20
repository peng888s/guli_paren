package com.pengguli.orderservice.client;

import com.pengguli.commonutils.order.CourseWebVoOrder;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.springframework.stereotype.Component;

//@Component
public class EduCourseClientImpl {
//    @Override
    public CourseWebVoOrder getCourseInfo(String courseId) {
        try{
            System.out.println("出错啦");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
