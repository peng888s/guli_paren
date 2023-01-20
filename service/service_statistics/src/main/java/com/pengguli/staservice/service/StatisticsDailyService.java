package com.pengguli.staservice.service;

import com.pengguli.staservice.pojo.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author peng
 * @since 2023-01-15
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void createStatisticsByDay(String day);

    // 图表显示，两部分数据JSON日期，JSON数据
    Map<String, Object> getShowDate(String type, String begin, String end);
}
