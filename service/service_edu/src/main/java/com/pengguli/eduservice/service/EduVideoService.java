package com.pengguli.eduservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pengguli.eduservice.pojo.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author peng
 * @since 2022-12-30
 */
public interface EduVideoService extends IService<EduVideo> {

    // 根据课程id删除小节
    void removeByVideoCourseId(String courseId);
}
