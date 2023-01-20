package com.pengguli.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.commonutils.R;
import com.pengguli.eduservice.pojo.EduTeacher;
import com.pengguli.eduservice.pojo.vo.TeacherQuery;
import com.pengguli.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author peng
 * @since 2022-12-25
 */
@RestController
@RequestMapping("/eduservice/teacher")
@Api(description="讲师管理")
//@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @GetMapping("findAll")
    @ApiOperation(value = "所有讲师列表")
    public R findAll(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true)
                           @PathVariable String id) {
        boolean flag = eduTeacherService.removeById(id);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "讲师分页功能")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(@PathVariable("current")long current,
                         @PathVariable("limit")long limit){
        Page<EduTeacher> eduTeacherPage = new Page<>(current,limit);
        eduTeacherService.page(eduTeacherPage,null);
        List<EduTeacher> records = eduTeacherPage.getRecords();  // 封装的分页数据
        long total = eduTeacherPage.getTotal();     // 总记录数
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation(value = "多条加组合查询+分页")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCurror(@PathVariable("current")long current,
                               @PathVariable("limit")long limit,
                               @RequestBody(required = false) TeacherQuery teacherQuery){
        // 创建Page对象
        Page<EduTeacher> eduTeacherPage = new Page<>(current,limit);
        // 构建条件
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        // 多条加组合查询
        if (!StringUtils.isEmpty(teacherQuery.getName()))
            queryWrapper.like("name",teacherQuery.getName());
        if (!StringUtils.isEmpty(teacherQuery.getLevel()))
            queryWrapper.eq("level",teacherQuery.getLevel());
        if (!StringUtils.isEmpty(teacherQuery.getBegin())) {
            queryWrapper.ge("gmt_create",teacherQuery.getBegin());
        }
        if (!StringUtils.isEmpty(teacherQuery.getEnd())) {
            queryWrapper.le("gmt_create",teacherQuery.getEnd());
        }
        queryWrapper.orderByDesc("gmt_create");
        eduTeacherService.page(eduTeacherPage,queryWrapper);
        // 封装的分页数据
        List<EduTeacher> records = eduTeacherPage.getRecords();
        // 总记录数
        long total = eduTeacherPage.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation(value = "修改前根据id查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable long id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("eduteacher",eduTeacher);
    }

    @ApiOperation(value = "修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }
    }


}

