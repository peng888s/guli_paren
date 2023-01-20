package com.pengguli.educms.service;

import com.pengguli.educms.pojo.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author peng
 * @since 2023-01-04
 */
public interface CrmBannerService extends IService<CrmBanner> {

    // 查询全部
    List<CrmBanner> selectAllBanner();
}
