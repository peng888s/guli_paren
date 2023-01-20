package com.pengguli.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pengguli.eduservice.pojo.EduChapter;
import com.pengguli.eduservice.mapper.EduChapterMapper;
import com.pengguli.eduservice.pojo.EduVideo;
import com.pengguli.eduservice.pojo.chapter.ChapterVo;
import com.pengguli.eduservice.pojo.chapter.VideVo;
import com.pengguli.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pengguli.eduservice.service.EduVideoService;
import com.pengguli.servicebase.exceptionhandler.GlobalExceptionHandler;
import com.pengguli.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author peng
 * @since 2022-12-30
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    // 课程大纲列表，根据id显示大纲下的小节
    @Override
    public List<ChapterVo> getAllChapterVideoByCourseId(String courseId) {
        List<ChapterVo> chapterVoList = new ArrayList<>();
        // 1、根据课程Id，获取该课程下的所有章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id",courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(chapterWrapper);

        // 2、根据章节id，获取该章节下的所有小节
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseId);
        List<EduVideo> eduVideos = videoService.list(videoQueryWrapper);

        // 3、遍历章节list集合进行封装
        eduChapters.forEach(eduChapter -> {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);

            // 4.遍历小节，封装
            List<VideVo> videVos = new ArrayList<>();
            eduVideos.forEach(eduVideo -> {
                if (chapterVo.getId().equals(eduVideo.getChapterId())){
                    VideVo videVo = new VideVo();
                    BeanUtils.copyProperties(eduVideo,videVo);
                    videVos.add(videVo);
                }
            });
            chapterVo.setVideVoList(videVos);
            chapterVoList.add(chapterVo);
        });
        return chapterVoList;
    }

    /**
     * 删除章节
     * @param chapterId 章节id
     * @return 如果章节下还有小节的话，该章节无法删除
     */
    @Override
    @Transactional
    public boolean deleteChapterById(String chapterId) {
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);
        int count = videoService.count(queryWrapper);
        if (count == 0){
            baseMapper.deleteById(chapterId);
            return true;
        }
        throw new RuntimeException("无法删除，请先处理该课程下的章节");
    }

    // 根据课程id删除章节
    @Override
    @Transactional
    public void removeChapterCourseId(String courseId) {
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        baseMapper.delete(chapterQueryWrapper);
    }
}
