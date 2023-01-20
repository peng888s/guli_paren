package com.pengguli.eduservice.controller;


import com.pengguli.commonutils.R;
import com.pengguli.eduservice.pojo.subject.OneSubject;
import com.pengguli.eduservice.pojo.subject.OneSubject;
import com.pengguli.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author peng
 * @since 2022-12-29
 */
@RestController
@RequestMapping("/eduservice/edu-subject")
@Api(description="课程管理")
//@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    @PostMapping("addSubject")
    @ApiOperation(value = "添加课程")
    public R addSubject(MultipartFile file){
        eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();
    }

    @GetMapping("finallySubject")
    @ApiOperation(value = "全部课程（树形结构）")
    public R finallySubject(){
        List<OneSubject> list = eduSubjectService.getAllSubject();
        return R.ok().data("list",list);
    }
}

