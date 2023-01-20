package com.pengguli.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pengguli.eduservice.client.VodClient;
import com.pengguli.eduservice.pojo.EduVideo;
import com.pengguli.eduservice.mapper.EduVideoMapper;
import com.pengguli.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author peng
 * @since 2022-12-30
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    // 删除课程时，根据课程id删除小节
    //TODO: 删除视频
    @Override
    public void removeByVideoCourseId(String courseId) {
        // 根据课程id获取所有视频id
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId).select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(queryWrapper);
        // List<EduVideo>变成List<String>
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)) {
                //放到videoIds集合里面
                videoIds.add(videoSourceId);
            }
        }
        if (videoIds.size() > 0){
            // 根据多个视频id删除视频
            vodClient.deleteBatch(videoIds);
        }

        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseId);
        baseMapper.delete(videoQueryWrapper);
    }
}
