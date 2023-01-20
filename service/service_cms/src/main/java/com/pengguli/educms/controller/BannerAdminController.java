package com.pengguli.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.educms.pojo.CrmBanner;
import com.pengguli.educms.service.CrmBannerService;
import com.pengguli.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 后台 管理员 banner
 * </p>
 *
 * @author peng
 * @since 2023-01-04
 */
@RestController
//@CrossOrigin
@RequestMapping("/educms/banneradmin")
public class BannerAdminController {

    @Autowired
    private CrmBannerService crmBannerService;

    // 分页
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page,@PathVariable long limit){
        Page<CrmBanner> bannerPage = new Page<>(page,limit);
        crmBannerService.page(bannerPage, null);
        return R.ok().data("items",bannerPage.getRecords()).data("total",bannerPage.getTotal());
    }

    // 添加
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.save(crmBanner);
        return R.ok();
    }

    // 删除
    @DeleteMapping("removeBanner/{id}")
    public R removeBanner(@PathVariable long id){
        crmBannerService.removeById(id);
        return R.ok();
    }

    // 根据id查询
    @GetMapping("getBanner/{id}")
    public R getBanner(@PathVariable long id){
        CrmBanner crmBanner = crmBannerService.getById(id);
        return R.ok().data("banner",crmBanner);
    }

    // 修改
    @PutMapping("updateBanner")
    public R updateBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.updateById(crmBanner);
        return R.ok();
    }

}

