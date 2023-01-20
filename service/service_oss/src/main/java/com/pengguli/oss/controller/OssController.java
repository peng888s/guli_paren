package com.pengguli.oss.controller;

import com.pengguli.commonutils.R;
import com.pengguli.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;

    @PostMapping
    public R uploadOssFile(@RequestPart("file") MultipartFile file){
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);
    }
}
