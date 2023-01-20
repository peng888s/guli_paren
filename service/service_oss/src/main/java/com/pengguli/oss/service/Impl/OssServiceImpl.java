package com.pengguli.oss.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.pengguli.oss.service.OssService;
import com.pengguli.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        String endpoint= ConstantPropertiesUtils.END_POIND;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            InputStream inputStream = file.getInputStream();
            // 获取文件名称 01.jsp
            String fileName = file.getOriginalFilename();
            // 生成唯一的值，防止文件名重复  wdfehsosj01.jsp
            String uuId = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuId + fileName;

            // 按照日期进行分类 2022/12/29/wdfehsosj01.jsp
            String datePth = new DateTime().toString("yyyy/MM/dd");
            fileName = datePth+"/"+fileName;

            bucketName= bucketName.replaceAll(" ","");
            // 创建PutObjectRequest对象。
            ossClient.putObject(bucketName, fileName, inputStream);
            // https://edu--peng.oss-cn-shanghai.aliyuncs.com/2022/12/29/wdfehsosj01.jsp
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
