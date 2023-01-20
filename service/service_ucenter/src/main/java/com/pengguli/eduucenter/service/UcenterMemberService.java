package com.pengguli.eduucenter.service;

import com.pengguli.eduucenter.pojo.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pengguli.eduucenter.pojo.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author peng
 * @since 2023-01-05
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    // 登录
    String loginChecked(UcenterMember ucenterMember);

    // 注册
    void register(RegisterVo registerVo);

    // 根据openid查找用户信息
    UcenterMember getOpenIdWx(String openid);

    // 查询某一天注册人数
    Integer countRegister(String day);
}
