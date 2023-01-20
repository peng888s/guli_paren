package com.pengguli.eduucenter.mapper;

import com.pengguli.eduucenter.pojo.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author peng
 * @since 2023-01-05
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    // 查询某一天注册人数
    Integer selectRegisterCount(String day);
}
