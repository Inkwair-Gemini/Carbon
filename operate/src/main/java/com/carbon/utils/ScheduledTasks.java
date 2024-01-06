package com.carbon.utils;

import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @projectName: Carbon
 * @package: com.carbon.utils
 * @className: ScheduledTasks
 * @author: Doctor.H
 * @description: TODO
 * @date: 2024/1/6 19:14
 */
@Component
public class ScheduledTasks {
    @Scheduled(cron = "0 0 16 * * ?")
    public void ListingDoneRecordTask(){
        System.out.println("定时任务");
    }
    @Scheduled(cron = "0 0 16 * * ?")
    public void DirectionDoneRecordTask(){
        System.out.println("定时任务");
    }
    @Scheduled(cron = "0 0 16 * * ?")
    public void GroupDoneRecordTask(){
        System.out.println("定时任务");
    }
    @Scheduled(cron = "0 0 16 * * ?")
    public void GroupClientTask(){
        System.out.println("定时任务");
    }
}
