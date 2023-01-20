package com.pengguli.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.eduservice.pojo.EduChapter;
import com.pengguli.eduservice.pojo.EduCourse;
import com.pengguli.eduservice.mapper.EduCourseMapper;
import com.pengguli.eduservice.pojo.EduCourseDescription;
import com.pengguli.eduservice.pojo.EduVideo;
import com.pengguli.eduservice.pojo.frontVo.CourseQueryVo;
import com.pengguli.eduservice.pojo.frontVo.CourseWebVo;
import com.pengguli.eduservice.pojo.vo.CourseInfoVo;
import com.pengguli.eduservice.pojo.vo.CourseListIf;
import com.pengguli.eduservice.pojo.vo.CoursePublishVo;
import com.pengguli.eduservice.service.EduChapterService;
import com.pengguli.eduservice.service.EduCourseDescriptionService;
import com.pengguli.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pengguli.eduservice.service.EduVideoService;
import com.pengguli.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author peng
 * @since 2022-12-30
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;
    @Autowired
    private EduVideoService videoService;
    @Autowired
    private EduChapterService chapterService;

    // 添加课程
    @Override
    @Transactional
    public String saveCourse(CourseInfoVo courseInfoVo) {
        // 向课程表中添加数据
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);

        // 向课程简介表中添加数据
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(eduCourse.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.save(eduCourseDescription);
        return eduCourse.getId();
    }

    // 根据id获取课程信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        // 获取课程信息
        EduCourse eduCourse = baseMapper.selectById(courseId);
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        // 获取课程简介
        EduCourseDescription eduCourseDescription = courseDescriptionService.getById(courseId);
        if (eduCourseDescription != null){
            courseInfoVo.setDescription(eduCourseDescription.getDescription());
        }
        return courseInfoVo;
    }

    // 修改课程信息
    @Override
    @Transactional
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        baseMapper.updateById(eduCourse);

        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        // 防止第一次添加之后没有填简介，返回修改填完简介后，是修改或添加
        int count = courseDescriptionService.count(new QueryWrapper<EduCourseDescription>().eq("id",eduCourseDescription.getId()));
        if (count ==1){
            courseDescriptionService.updateById(eduCourseDescription);
        }else {
            courseDescriptionService.save(eduCourseDescription);
        }

    }

    // 根据课程id查询最终确认的课程信息
    @Override
    public CoursePublishVo getCoursePublishVo(String id) {
        CoursePublishVo courseInfo = baseMapper.getPublishCourseInfo(id);
        return courseInfo;
    }

    // 分页查询
    @Override
    public Page<EduCourse> pageCourse(long current, long limit, CourseListIf eduCourse) {
        Page<EduCourse> coursePage = new Page<>(current,limit);
        LambdaQueryWrapper<EduCourse> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseLambdaQueryWrapper.like(!StringUtils.isEmpty(eduCourse.getTitle()),EduCourse::getTitle,eduCourse.getTitle());
        courseLambdaQueryWrapper.eq(!StringUtils.isEmpty(eduCourse.getStatus()),EduCourse::getStatus,eduCourse.getStatus());
        baseMapper.selectPage(coursePage, courseLambdaQueryWrapper);
        return coursePage;
    }

    // 删除小节，描述，章节，课程，根据课程id
    @Override
    @Transactional
    public void removeCourseId(String courseId) {
        // 删除小节
        videoService.removeByVideoCourseId(courseId);

        // 删除章节
        chapterService.removeChapterCourseId(courseId);

        // 删除描述
        courseDescriptionService.removeById(courseId);

        // 删除课程
        int result = baseMapper.deleteById(courseId);
        if (result == 0){
            throw new GuliException(20001,"删除失败");
        }
    }

    // 前八门热门课程
    @Override
    @Cacheable(value = "course",key = "'courseList'")
    public List<EduCourse> selectFrontCourse() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id").last("limit 8");
        List<EduCourse> courses = baseMapper.selectList(wrapper);
        return courses;
    }

    // 条件查询 ，前端
    @Override
    public Map<String,Object> getCourseConditionIn(Page<EduCourse> pageParam, CourseQueryVo courseQueryVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        // 根据一级分类查询课程
        if (!StringUtils.isEmpty(courseQueryVo.getSubjectParentId())){
            wrapper.eq("subject_parent_id",courseQueryVo.getSubjectParentId());
        }
        // 根据二级分类查询
        if(!StringUtils.isEmpty(courseQueryVo.getSubjectId())) wrapper.eq("subject_id",courseQueryVo.getSubjectId());
        // 根据关注度查询
        if (!StringUtils.isEmpty(courseQueryVo.getBuyCountSort())){
            wrapper.orderByDesc("buy_count");
        }
        // 根据上线时间查询
        if (!StringUtils.isEmpty(courseQueryVo.getGmtCreateSort())){
            wrapper.orderByDesc("gmt_create");
        }
        // 根据价格查询
        if (!StringUtils.isEmpty(courseQueryVo.getPriceSort())){
            wrapper.orderByAsc("price");
        }
        baseMapper.selectMapsPage(pageParam,wrapper);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items",pageParam.getRecords()); // 每页数据
        map.put("current",pageParam.getCurrent()); // 当前页
        map.put("pages",pageParam.getPages()); // 总页数
        map.put("size",pageParam.getSize());
        map.put("total",pageParam.getTotal()); // 总记录数
        map.put("hasNext",pageParam.hasNext()); // 是否有下一页
        map.put("hasPrevious",pageParam.hasPrevious()); // 是否有上一页
        return map;
    }

    @Override
    public CourseWebVo getInfoWebById(String courseId) {
        CourseWebVo courseWebVo = baseMapper.selectInfoWebById(courseId);
        return courseWebVo;
    }
}
/**
 *   QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
 *         if (!StringUtils.isEmpty(title)){
 *             queryWrapper.like("title",title);
 *         }
 *         if (!StringUtils.isEmpty(status)){
 *             queryWrapper.eq("status",status);
 *         }
 */