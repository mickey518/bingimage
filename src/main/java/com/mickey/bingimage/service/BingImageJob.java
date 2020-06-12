package com.mickey.bingimage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mickey.bingimage.api.BingImageController;
import com.mickey.bingimage.dto.Image;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * #Description
 *
 * @author wangmeng
 * @date 2020-06-12
 */
@Slf4j
public class BingImageJob implements Job {
    @Autowired
    private BingImageController controller;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {

            log.info("-------------------------------------getting bing image-----------------------------");
            List<Image> images = controller.pullImages();
            log.info(JSON.toJSONString(images, SerializerFeature.PrettyFormat));
            log.info("next run time " + jobExecutionContext.getNextFireTime());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
