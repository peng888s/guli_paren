package com.pengguli.vod.service.Impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.pengguli.servicebase.exceptionhandler.GuliException;
import com.pengguli.vod.controller.VodController;
import com.pengguli.vod.service.VodService;
import com.pengguli.vod.utils.InitVodClient;
import com.pengguli.vod.utils.VodConstantUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    // 上传视频
    @Override
    public String uploadVideoAly(MultipartFile file) {
        String videoId;
        try {
            // 1、获上传文件原始名称(必选)
            String fileName = file.getOriginalFilename();
            // 2、上传之后显示的名称(必选)
            String title = fileName.substring(0,fileName.lastIndexOf("."));
            // 3、上传文件输入流(必选)
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(VodConstantUtils.ACCESS_KEY_ID,VodConstantUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);
            // 4、创建实现上传的对象
            UploadVideoImpl uploader = new UploadVideoImpl();
            // 5、上传动作，到这一步，已经上传成功
            UploadStreamResponse response = uploader.uploadStream(request);
            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
        }catch (Exception e){
            return null;
        }
        return videoId;
    }

    // 删除视频
    @Override
    public void removeVideoById(String id) {
        // 获取初始化对象
        DefaultAcsClient client = InitVodClient.initVodClient(VodConstantUtils.ACCESS_KEY_ID, VodConstantUtils.ACCESS_KEY_SECRET);
        // 删除视频request对象
        DeleteVideoRequest request = new DeleteVideoRequest();
        // 设置视频id
        request.setVideoIds(id);
        try {
            // 调用初始化对象的方法实现删除
            client.getAcsResponse(request);
        } catch (com.aliyuncs.exceptions.ClientException e) {
            throw new GuliException(20001,"删除视频失败");
        }
    }

    @Override
    public void removeMoreAlyVideo(List<String> videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(VodConstantUtils.ACCESS_KEY_ID, VodConstantUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //videoIdList值转换成 1,2,3
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");

            //向request设置视频id
            request.setVideoIds(videoIds);
            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);
        }catch(Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }

    // 根据视频id获取视频凭证
    @Override
    public String getPlayAuth(String id) {
        // 1、创建初始化对象
        DefaultAcsClient client =
                InitVodClient.initVodClient(VodConstantUtils.ACCESS_KEY_ID, VodConstantUtils.ACCESS_KEY_SECRET);
        // 2、创建设置凭证的request对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        // 3、设置视频id
        request.setVideoId(id);
        // 4、创建获取凭证的response对象
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        // 获取视频凭证
        try {
            response = client.getAcsResponse(request);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        String playAuth = response.getPlayAuth();
        return playAuth;
    }
}
