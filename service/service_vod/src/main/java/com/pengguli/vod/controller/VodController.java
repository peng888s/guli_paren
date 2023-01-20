package com.pengguli.vod.controller;

import com.pengguli.commonutils.R;
import com.pengguli.vod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Path;
import java.util.List;

@Api(description = "阿里云视频上传")
@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    @PostMapping("uploadVideo")
    @ApiOperation("上传视频")
    public R uploadVideoAly(MultipartFile file){
        String videoId = vodService.uploadVideoAly(file);
        return R.ok().data("videoId",videoId);
    }

    @DeleteMapping("removeVideo/{id}")
    @ApiOperation("删除视频")
    public R removeVideo(@PathVariable("id") String id){
        vodService.removeVideoById(id);
        return R.ok();
    }

    //删除多个阿里云视频的方法
    //参数多个视频id  List videoIdList
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList) {
        vodService.removeMoreAlyVideo(videoIdList);
        return R.ok();
    }

    // 根据视频id获取视频凭证
    @GetMapping("getPlay/{id}")
    public R getPlay(@PathVariable String id){
        String play = vodService.getPlayAuth(id);
        return R.ok().data("play",play);
    }
}
