package com.pengguli.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.eduservice.pojo.EduTeacher;
import com.pengguli.eduservice.mapper.EduTeacherMapper;
import com.pengguli.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author peng
 * @since 2022-12-25
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    // 前四位热门老师
    @Override
    @Cacheable(value = "teacher",key = "'teacherList'")
    public List<EduTeacher> selectFrontTeacher() {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id").last("limit 4");
        List<EduTeacher> teachers = baseMapper.selectList(wrapper);
        return teachers;
    }

    // 分页查询全部老师
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageParam) {
        // 按照id降序排列
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        baseMapper.selectPage(pageParam, wrapper);

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
}
