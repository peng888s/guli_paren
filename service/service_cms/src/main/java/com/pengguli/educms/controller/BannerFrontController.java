package com.pengguli.educms.controller;


import com.pengguli.educms.pojo.CrmBanner;
import com.pengguli.educms.service.CrmBannerService;
import com.pengguli.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前台 普通用户 banner
 * </p>
 *
 * @author peng
 * @since 2023-01-04
 */
@RestController
//@CrossOrigin
@RequestMapping("/educms/bannerfront")
public class BannerFrontController {

    @Autowired
    private CrmBannerService crmBannerService;

    // 查询全部
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> banners = crmBannerService.selectAllBanner();
        return R.ok().data("list",banners);
    }
}

