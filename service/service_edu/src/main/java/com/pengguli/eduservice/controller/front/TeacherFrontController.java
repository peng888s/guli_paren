package com.pengguli.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.commonutils.R;
import com.pengguli.eduservice.pojo.EduCourse;
import com.pengguli.eduservice.pojo.EduTeacher;
import com.pengguli.eduservice.service.EduCourseService;
import com.pengguli.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/teacherfront")
//@CrossOrigin
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;

    @PostMapping("getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontList(@PathVariable long page,@PathVariable long limit){
        Page<EduTeacher> eduTeacherPage = new Page<>(page,limit);
        Map<String,Object> map = teacherService.getTeacherFrontList(eduTeacherPage);
        System.out.println(map);
        return R.ok().data(map);
    }

    // 讲师详情
    @GetMapping("getTeacherFrontInfo/{id}")
    public R getTeacherFrontInfo(@PathVariable String id){
        // 1、根据讲师id，查询讲师信息
        EduTeacher teacher = teacherService.getById(id);
        // 2、根据讲师id，查询所讲课程信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",id);
        List<EduCourse> list = courseService.list(wrapper);
        return R.ok().data("teacher",teacher).data("list",list);
    }
}
