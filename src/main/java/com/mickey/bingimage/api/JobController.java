package com.mickey.bingimage.api;

import com.mickey.bingimage.service.BingImageJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * #Description
 *
 * @author wangmeng
 * @date 2020-06-11
 */
@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {
    /**
     * 加入Qualifier注解，通过名称注入bean
     */
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
        log.info("----------------------------------------------------- init job -----------------------------------------------------");

        String jobName = "BingImage";
        String groupName = "Image";

        scheduler.start();
        JobDetail jobDetail = JobBuilder.newJob(BingImageJob.class)
                .withIdentity(jobName, groupName)
                .withDescription("get bing.cn background image in everyday")
                .build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 11 * * ? ");
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, groupName)
                .withSchedule(scheduleBuilder).build();
        Date date = scheduler.scheduleJob(jobDetail, trigger);

        log.info(String.format("%s next run time is %tc", jobName, date));
    }
}
