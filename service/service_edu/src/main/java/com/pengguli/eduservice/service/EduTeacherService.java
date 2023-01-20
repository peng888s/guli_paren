package com.pengguli.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.eduservice.pojo.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author peng
 * @since 2022-12-25
 */
public interface EduTeacherService extends IService<EduTeacher> {

    // 前四位热门老师
    List<EduTeacher> selectFrontTeacher();

    // 分页查询全部老师
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> eduTeacherPage);
}
