package com.pengguli.eduservice.controller;


import com.pengguli.commonutils.R;
import com.pengguli.eduservice.client.VodClient;
import com.pengguli.eduservice.pojo.EduVideo;
import com.pengguli.eduservice.service.EduVideoService;
import com.pengguli.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author peng
 * @since 2022-12-30
 */
@RestController
@RequestMapping("/eduservice/edu-video")
@Api(description = "小节管理")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    // 添加小节
    @PostMapping("addVideo")
    @ApiOperation(value = "添加小节")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok().data("eduVideo",eduVideo);
    }

    // 删除小节
    // TODO 需要完善，删除小节的时候，同时把视频删掉
    @DeleteMapping("deleteVideo/{videoId}")
    @ApiOperation(value = "删除小节")
    public R deleteVideo(@PathVariable String videoId){
        // 根据小节id获取视频id
        EduVideo video = eduVideoService.getById(videoId);
        if (!StringUtils.isEmpty(video.getVideoSourceId())){
            // 视频id不为空，删除视频
            R result = vodClient.removeAlyVideo(video.getVideoSourceId());
            if (result.getCode() == 20001){
                throw new GuliException(20001,"失败");
            }
        }
        eduVideoService.removeById(videoId);
        return R.ok();
    }

    // 根据id获取小节
    @GetMapping("getVideo/{videoId}")
    @ApiOperation(value = "根据id获取小节")
    public R getVideo(@PathVariable String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return R.ok().data("video",eduVideo);
    }

    // 修改小节
    @PutMapping("updateVideo")
    @ApiOperation(value = "修改小节")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

}

