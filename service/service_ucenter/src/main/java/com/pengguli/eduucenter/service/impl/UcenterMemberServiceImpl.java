package com.pengguli.eduucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pengguli.commonutils.JwtUtils;
import com.pengguli.commonutils.MD5;
import com.pengguli.eduucenter.pojo.UcenterMember;
import com.pengguli.eduucenter.mapper.UcenterMemberMapper;
import com.pengguli.eduucenter.pojo.vo.RegisterVo;
import com.pengguli.eduucenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pengguli.servicebase.exceptionhandler.GuliException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author peng
 * @since 2023-01-05
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 登录
    @Override
    public String loginChecked(UcenterMember ucenterMember) {
        // 判断手机号 密码是否为空
        if(StringUtils.isEmpty(ucenterMember.getMobile()) || StringUtils.isEmpty(ucenterMember.getPassword())){
            throw new GuliException(20001,"手机号或密码不能为空");
        }
        // 根据手机号，获取用户信息
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",ucenterMember.getMobile());
        UcenterMember member = baseMapper.selectOne(wrapper);
        System.out.println(member.getPassword());
        // 对象
        if (member == null){
            throw new GuliException(20001,"手机号不存在");
        }
        // 判断密码是否正确
        String password = MD5.encrypt(ucenterMember.getPassword());
        System.out.println(password);
        if (!password.equals(member.getPassword())){
            throw new GuliException(20001,"密码错误，请重新输入，加密");
        }
        // 校验账号是否被禁用
        if (member.getIsDisabled()){
            throw new GuliException(20001,"改账号已被封禁");
        }
        // 校验成功，返回token字符串，将主键id存入token
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return token;
    }

    // 注册
    @Override
    @Transactional
    public void register(RegisterVo registerVo) {
        // 判断非空
        if (StringUtils.isEmpty(registerVo.getPassword()) ||
                StringUtils.isEmpty(registerVo.getNickname()) ||
                StringUtils.isEmpty(registerVo.getMobile()) ||
                StringUtils.isEmpty(registerVo.getCode())){
            throw new GuliException(20001,"不允许为空，注册为空！");
        }
        // 判断手机号是否已经存在
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",registerVo.getMobile());
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0){
            throw new GuliException(20001,"该手机号已经注册过账号");
        }

        // 判断验证码
        String code = redisTemplate.opsForValue().get(registerVo.getMobile());
        if (!code.equals(registerVo.getCode())) throw new GuliException(20001,"验证码错误，或已失效");

        UcenterMember member = new UcenterMember();
        BeanUtils.copyProperties(registerVo,member);
        member.setAvatar("https://guli-file-190513.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        member.setIsDisabled(false);
        member.setPassword(MD5.encrypt(member.getPassword()));

        // 添加
        baseMapper.insert(member);
    }

    // 根据openid查找用户信息
    @Override
    public UcenterMember getOpenIdWx(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    // 查询某一天注册人数
    @Override
    public Integer countRegister(String day) {
        Integer count = baseMapper.selectRegisterCount(day);
        return count;
    }
}
