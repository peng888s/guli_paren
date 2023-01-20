package com.pengguli.eduucenter.controller;


import com.pengguli.commonutils.JwtUtils;
import com.pengguli.commonutils.R;
import com.pengguli.commonutils.UcenterMemberVo;
import com.pengguli.commonutils.order.UcenterMemberOrder;
import com.pengguli.eduucenter.pojo.UcenterMember;
import com.pengguli.eduucenter.pojo.vo.RegisterVo;
import com.pengguli.eduucenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author peng
 * @since 2023-01-05
 */
@RestController
//@CrossOrigin
@RequestMapping("/eduucenter/member")
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //login
    @PostMapping("login")
    public R login(@RequestBody UcenterMember ucenterMember){
        String token = memberService.loginChecked(ucenterMember);
        return R.ok().data("token",token);
    }

    // register
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    // 根据token字符串获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember ucenterMember = memberService.getById(memberId);
        return R.ok().data("member",ucenterMember);
    }

    //根据用户id查询用户信息
    @PostMapping("/getMemberInfoById/{memberId}")
    public UcenterMemberVo getMemberInfoById(@PathVariable String memberId){
        UcenterMember member = memberService.getById(memberId);
        UcenterMemberVo memberVo = new UcenterMemberVo();
        BeanUtils.copyProperties(member,memberVo);
        return memberVo;
    }

    //根据用户id查询用户信息
    @PostMapping("/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember member = memberService.getById(id);
        UcenterMemberOrder memberVo = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,memberVo);
        return memberVo;
    }

    // 查询某一天注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count = memberService.countRegister(day);
        return R.ok().data("count",count);
    }

}

