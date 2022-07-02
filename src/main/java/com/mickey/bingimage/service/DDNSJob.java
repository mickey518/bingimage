package com.mickey.bingimage.service;

import com.mickey.bingimage.api.DDNSController;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.annotation.Resource;

@Slf4j
public class DDNSJob implements Job {
    @Resource
    private DDNSController controller;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        log.info("-------------------------------------sync DDNS-----------------------------");
        String result = controller.syncDns();
        log.info("next run time " + jobExecutionContext.getNextFireTime());
    }
}
