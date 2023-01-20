package com.pengguli.staservice.schedule;

import com.pengguli.staservice.service.StatisticsDailyService;
import com.pengguli.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService service;

    // 每天凌晨一点把前一天的数据添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task(){
        service.createStatisticsByDay(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }
}
