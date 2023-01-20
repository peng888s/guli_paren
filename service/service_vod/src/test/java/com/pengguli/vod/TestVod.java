package com.pengguli.vod;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;

import java.util.List;

public class TestVod {
    // 上传视频
    public static void main(String[] args) throws Exception{
//        String accessKeyId = "LTAI5tM1q5FxS5cFoaGhTatq";
//        String accessKeySecret = "g1oLrhTX2O6TIftPEZ7jGlepuIoWDm";
//        String title = "test"; // 上传之后，文件名称
//        String fileName = "D:\\项目\\谷粒学苑\\项目资料\\1-阿里云上传测试视频\\6 - What If I Want to Move Faster.mp4"; // 本地文件路径和名称
//        testUploadVideo(accessKeyId,accessKeySecret,title,fileName);
        getPlayAuth();
    }

    // 本地文件上传接口
    private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，（注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }


    // 根据视频id获取视频播放凭证
    public static void getPlayAuth() throws Exception{
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tM1q5FxS5cFoaGhTatq", "g1oLrhTX2O6TIftPEZ7jGlepuIoWDm");
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        request.setVideoId("62f2e6608c3371ed800f0764a3fc0102");
        response = client.getAcsResponse(request);
        System.out.println("视频播放凭证："+response.getPlayAuth());
    }

    // 根据视频id获取视频播放地址，但如果视频进行啦加密，只根据视频地址无法播放
    public static void getPlayUrl() throws Exception{
        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tM1q5FxS5cFoaGhTatq", "g1oLrhTX2O6TIftPEZ7jGlepuIoWDm");

        //创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        //向request对象里面设置视频id
        request.setVideoId("b12050e089e371ed9ad50764a3fc0102");

        //调用初始化对象里面的方法，传递request，获取数据
        response = client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }
}
