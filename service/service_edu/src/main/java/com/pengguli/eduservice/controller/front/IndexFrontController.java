package com.pengguli.eduservice.controller.front;

import com.pengguli.commonutils.R;
import com.pengguli.eduservice.pojo.EduCourse;
import com.pengguli.eduservice.pojo.EduTeacher;
import com.pengguli.eduservice.service.EduCourseService;
import com.pengguli.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
//@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    // 查询前四名热门老师，前八名热门课程
    @GetMapping("index")
    public R index(){
        // 前四位热门老师
        List<EduTeacher> teachers = eduTeacherService.selectFrontTeacher();
        // 前八门热门课程
        List<EduCourse> courses = eduCourseService.selectFrontCourse();
        return R.ok().data("eduList",courses).data("teacherList",teachers);
    }
}
