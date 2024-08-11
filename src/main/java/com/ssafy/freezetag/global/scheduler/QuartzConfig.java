package com.ssafy.freezetag.global.scheduler;


import com.ssafy.freezetag.global.scheduler.job.DailyRoomBatchJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(DailyRoomBatchJob.class)
                .withIdentity("dailyRoomBatchJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("dailyRoomBatchTrigger")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 0))  // 매일 자정에 실행
                .build();
    }
}

