package com.pengguli.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.commonutils.R;
import com.pengguli.eduservice.pojo.EduCourse;
import com.pengguli.eduservice.pojo.vo.CourseInfoVo;
import com.pengguli.eduservice.pojo.vo.CourseListIf;
import com.pengguli.eduservice.pojo.vo.CoursePublishVo;
import com.pengguli.eduservice.pojo.vo.TeacherQuery;
import com.pengguli.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author peng
 * @since 2022-12-30
 */
@RestController
@RequestMapping("/eduservice/edu-course")
@Api(description = "课程管理")
//@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @PostMapping("pageCourseList/{current}/{limit}")
    @ApiModelProperty(value = "分页查询")
    public R pageCourseList(@PathVariable("current")long current,
                            @PathVariable("limit")long limit,
                            @RequestBody(required = false) CourseListIf eduCourse){
        Page<EduCourse> coursePage = courseService.pageCourse(current,limit,eduCourse);
        List<EduCourse> courseList = coursePage.getRecords();
        return R.ok().data("list",courseList).data("total",coursePage.getTotal());
    }

    @PostMapping("addCourse")
    @ApiOperation(value = "添加课程")
    public R addCourse(@RequestBody CourseInfoVo courseInfoVo){
        String courseId = courseService.saveCourse(courseInfoVo);
        return R.ok().data("courseId",courseId);
    }

    @GetMapping("getCourseInfo/{courseId}")
    @ApiOperation(value = "根据id获取课程信息")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    @PostMapping("updateCourseInfo")
    @ApiOperation(value = "修改课程信息")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    @ApiOperation(value = "根据课程id查询最终确认的课程信息")
    @GetMapping("getCoursePublish/{courseId}")
    public R getCoursePublish(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(courseId);
        return R.ok().data("coursePublishVo",coursePublishVo);
    }

    @ApiOperation(value = "课程最终发布")
    @PostMapping("publishCourse/{courseId}")
    public R publishCourse(@PathVariable String courseId){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");  // 设置课程发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

    @ApiOperation(value = "删除小节，描述，章节，课程，根据课程id")
    @DeleteMapping("deleteCourseId/{courseId}")
    public R deleteCourseId(@PathVariable String courseId){
        courseService.removeCourseId(courseId);
        return R.ok();
    }


}

