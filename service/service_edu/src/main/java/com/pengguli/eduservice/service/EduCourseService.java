package com.pengguli.eduservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.eduservice.pojo.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pengguli.eduservice.pojo.frontVo.CourseQueryVo;
import com.pengguli.eduservice.pojo.frontVo.CourseWebVo;
import com.pengguli.eduservice.pojo.vo.CourseInfoVo;
import com.pengguli.eduservice.pojo.vo.CourseListIf;
import com.pengguli.eduservice.pojo.vo.CoursePublishVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author peng
 * @since 2022-12-30
 */
public interface EduCourseService extends IService<EduCourse> {

    // 添加课程
    String saveCourse(CourseInfoVo courseInfoVo);

    // 根据课程id，获取课程信息
    CourseInfoVo getCourseInfo(String courseId);

    // 修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    // 根据课程id查询最终确认的课程信息
    CoursePublishVo getCoursePublishVo(String id);

    // 分页查询
    Page<EduCourse> pageCourse(long current, long limit, CourseListIf eduCourse);

    // 删除小节，描述，章节，课程，根据课程id
    void removeCourseId(String courseId);

    // 前八门热门课程
    List<EduCourse> selectFrontCourse();

    // 条件查询
    Map<String,Object> getCourseConditionIn(Page<EduCourse> pageParam, CourseQueryVo courseQueryVo);

    // 根据课程id查询课程全部信息
    CourseWebVo getInfoWebById(String courseId);
}
