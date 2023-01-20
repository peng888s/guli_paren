package com.pengguli.eduservice.controller;


import com.pengguli.commonutils.R;
import com.pengguli.eduservice.pojo.EduChapter;
import com.pengguli.eduservice.pojo.chapter.ChapterVo;
import com.pengguli.eduservice.service.EduChapterService;
import com.pengguli.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eduservice/edu-chapter")
@Api(description = "章节管理")
//@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    // 课程大纲列表，根据id显示大纲下的小节
    @GetMapping("allChapterVideo/{courseId}")
    @ApiOperation(value = "全部章节，以及对应小结")
    public R getAllChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list = eduChapterService.getAllChapterVideoByCourseId(courseId);
        return R.ok().data("chapterVideo",list);
    }

    // 添加章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.save(eduChapter);
        return R.ok().data("eduChapter",eduChapter);
    }

    // 根据id查询章节
    @GetMapping("getChapter/{chapterId}")
    public R getChapterById(@PathVariable String chapterId){
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return R.ok().data("eduChapter",eduChapter);
    }

    // 修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.updateById(eduChapter);
        return R.ok().data("eduChapter",eduChapter);
    }

    // 删除章节
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag = eduChapterService.deleteChapterById(chapterId);
        if (!flag){
            return R.error();
        }
        return R.ok();
    }

}

