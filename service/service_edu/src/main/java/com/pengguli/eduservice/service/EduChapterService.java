package com.pengguli.eduservice.service;

import com.pengguli.eduservice.pojo.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pengguli.eduservice.pojo.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author peng
 * @since 2022-12-30
 */
public interface EduChapterService extends IService<EduChapter> {

    // 课程大纲列表，根据id显示大纲下的小节
    List<ChapterVo> getAllChapterVideoByCourseId(String courseId);

    // 删除章节
    boolean deleteChapterById(String chapterId);

    // 根据课程id删除章节
    void removeChapterCourseId(String courseId);
}
