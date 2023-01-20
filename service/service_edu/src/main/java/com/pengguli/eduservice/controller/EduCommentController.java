package com.pengguli.eduservice.controller;


import com.alibaba.nacos.client.naming.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.commonutils.JwtUtils;
import com.pengguli.commonutils.R;
import com.pengguli.commonutils.UcenterMemberVo;
import com.pengguli.eduservice.client.UcenterClient;
import com.pengguli.eduservice.pojo.EduComment;
import com.pengguli.eduservice.service.EduCommentService;
import com.pengguli.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author peng
 * @since 2023-01-09
 */
//@CrossOrigin
@RestController
@RequestMapping("/eduservice/edu-comment")
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterClient ucenterClient;

    //根据课程id_分页查询课程评论的方法
    @GetMapping("getCommentPage/{page}/{limit}")
    public R getCommentPage(@PathVariable long page,@PathVariable long limit,String courseId){
        Page<EduComment> pageParam = new Page<>(page,limit);
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        //判断课程id是否为空
        if (!StringUtils.isEmpty(courseId)){
            wrapper.eq("course_id",courseId);
        }
        //按最新排序
        wrapper.orderByDesc("gmt_create");
        commentService.page(pageParam,wrapper);
        List<EduComment> commentList = pageParam.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }

    // 添加评论
    @PostMapping("saveComment")
    public R saveComment(HttpServletRequest request, @RequestBody EduComment comment){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("token:"+id);
        if (StringUtils.isEmpty(id)){
            return R.error().code(28004).message("请登录");
        }
        comment.setMemberId(id);
        //远程调用ucenter根据用户id获取用户信息

        UcenterMemberVo memberInfoById = ucenterClient.getMemberInfoById(id);
//        comment.setAvatar(memberInfoById.getAvatar());
//        comment.setNickname(memberInfoById.getNickname());

        BeanUtils.copyProperties(memberInfoById,comment);
        System.out.println(memberInfoById);
        comment.setId("");
        commentService.save(comment);
        return R.ok();
    }
}

