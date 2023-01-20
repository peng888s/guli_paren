package com.pengguli.staservice.controller;


import com.pengguli.commonutils.R;
import com.pengguli.staservice.client.UcenterMemberClient;
import com.pengguli.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author peng
 * @since 2023-01-15
 */
@RestController
//@CrossOrigin
@RequestMapping("/staservice/statistics-daily")
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService dailyService;

    // 统计某一天的注册人数，生成统计数据
    @PostMapping("{day}")
    public R createStatisticsByDate(@PathVariable String day) {
        dailyService.createStatisticsByDay(day);
        return R.ok();
    }

    // 图表显示，两部分数据JSON日期，JSON数据
    @GetMapping("getShowDate/{type}/{begin}/{end}")
    public R getShowDate(@PathVariable String type,
                         @PathVariable String begin,
                         @PathVariable String end){
        Map<String,Object> map = dailyService.getShowDate(type,begin,end);
        return R.ok().data(map);
    }

}