package com.pengguli.eduservice.mapper;

import com.pengguli.eduservice.pojo.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pengguli.eduservice.pojo.frontVo.CourseWebVo;
import com.pengguli.eduservice.pojo.vo.CourseInfoVo;
import com.pengguli.eduservice.pojo.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author peng
 * @since 2022-12-30
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getPublishCourseInfo(String courseId);

    public CourseWebVo selectInfoWebById(String courseId);
}
