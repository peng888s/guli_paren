package com.pengguli.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengguli.commonutils.JwtUtils;
import com.pengguli.commonutils.R;
import com.pengguli.commonutils.order.CourseWebVoOrder;
import com.pengguli.eduservice.client.OrdersClient;
import com.pengguli.eduservice.pojo.EduCourse;
import com.pengguli.eduservice.pojo.chapter.ChapterVo;
import com.pengguli.eduservice.pojo.frontVo.CourseQueryVo;
import com.pengguli.eduservice.pojo.frontVo.CourseWebVo;
import com.pengguli.eduservice.service.EduChapterService;
import com.pengguli.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrdersClient ordersClient;

    @PostMapping("getCoursePage/{page}/{limit}")
    public R getCoursePage(@RequestBody(required = false) CourseQueryVo courseQueryVo,@PathVariable long page,@PathVariable long limit){
        Page<EduCourse> pageParam = new Page<>(page,limit);
        Map<String,Object> eduCourses = courseService.getCourseConditionIn(pageParam,courseQueryVo);
        return R.ok().data(eduCourses);
    }

    // 课程详情
    @GetMapping("getCourseFrontInfo/{courseId}")
    public R getCourseFrontInfo(@PathVariable String courseId, HttpServletRequest request){
        // 根据课程id查询课程全部信息
        CourseWebVo courseWebVo = courseService.getInfoWebById(courseId);
        // 对应课程下的所有章节小节
        List<ChapterVo> allChapterVideoByCourseId = chapterService.getAllChapterVideoByCourseId(courseId);
        // 判断是否登录
        if (JwtUtils.getMemberIdByJwtToken(request) == null){
            return R.ok().code(28004).message("请先登录");
        }
        // 查看课程是否已经购买
        boolean isBuy = ordersClient.getOrderStatus(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("courseWebVo",courseWebVo).
                data("allChapterVideoByCourseId",allChapterVideoByCourseId)
                .data("isBuyCourse",isBuy);
    }

    @PostMapping("getCourseInfo/{courseId}")
    public CourseWebVoOrder getCourseInfo(@PathVariable String courseId){
        System.out.println("执行啦");
        CourseWebVo infoWebById = courseService.getInfoWebById(courseId);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(infoWebById,courseWebVoOrder);
        System.out.println(courseWebVoOrder.toString());
        return courseWebVoOrder;
    }
}
