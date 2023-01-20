package com.pengguli.staservice.service.impl;

import com.alibaba.nacos.client.utils.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pengguli.commonutils.R;
import com.pengguli.staservice.client.UcenterMemberClient;
import com.pengguli.staservice.pojo.StatisticsDaily;
import com.pengguli.staservice.mapper.StatisticsDailyMapper;
import com.pengguli.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author peng
 * @since 2023-01-15
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterMemberClient ucenterMemberClient;

    @Override
    @Transactional
    public void createStatisticsByDay(String day) {
        //删除已存在的统计对象，保持每次添加的数据都是最新的、不重复的
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);

        // 远程调用，获取某一天的注册人数
        R r = ucenterMemberClient.countRegister(day);
        Integer count = (Integer) r.getData().get("count");

        // 模拟登陆人数，观看视频人数，上传课程人数
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO

        // 添加数据库，统计分析表
        StatisticsDaily daily = new StatisticsDaily();
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setRegisterNum(count);
        daily.setDateCalculated(day);

        baseMapper.insert(daily);
    }

    // 图表显示，两部分数据JSON日期，JSON数据
    @Override
    public Map<String, Object> getShowDate(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        // 从那个时间开始、那个时间结束
        wrapper.between("date_calculated",begin,end);
        // 获取的是那部分数据
        wrapper.select("date_calculated",type);
        wrapper.orderByAsc("date_calculated");
        List<StatisticsDaily> dailys = baseMapper.selectList(wrapper);

        // 封装数据，前端要求的是JSON字符串，两部分数据：日期、日期对应的数据
        Map<String,Object> map = new HashMap<>();
        // list集合回到前端自动转换成JSON字符串
        // 存储数据list
        List<Integer> dataList = new ArrayList<>();
        // 存储日期list
        List<String> dateList = new ArrayList<>();
        map.put("dataList", dataList);
        map.put("dateList", dateList);
        dailys.forEach(statisticsDaily -> {
            dateList.add(statisticsDaily.getDateCalculated()); // 日期
            switch (type){
                case "register_num":
                    dataList.add(statisticsDaily.getRegisterNum());
                    break;
                case "login_num":
                    dataList.add(statisticsDaily.getLoginNum());
                    break;
                case "video_view_num":
                    dataList.add(statisticsDaily.getVideoViewNum());
                    break;
                case "course_num":
                    dataList.add(statisticsDaily.getCourseNum());
                    break;
                default:
                    break;
            }
        });
        return map;
    }
}
