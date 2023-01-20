package com.pengguli.orderservice.client;

import com.pengguli.commonutils.order.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "service-edu")
public interface EduCourseClient {
    @PostMapping("/eduservice/coursefront/getCourseInfo/{courseId}")
    public CourseWebVoOrder getCourseInfo(@PathVariable("courseId") String courseId);
}