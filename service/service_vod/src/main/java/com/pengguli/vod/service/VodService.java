package com.pengguli.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    // 上传视频
    String uploadVideoAly(MultipartFile file);

    // 删除视频
    void removeVideoById(String id);

    void removeMoreAlyVideo(List<String> videoIdList);

    // 根据视频id获取视频凭证
    String getPlayAuth(String id);
}
