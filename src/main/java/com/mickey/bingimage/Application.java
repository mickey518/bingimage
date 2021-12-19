package com.mickey.bingimage;

import com.mickey.bingimage.api.BingImageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * #Description
 *
 * @author wangmeng
 * @date 2020-06-12
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        BingImageController imageController = new BingImageController();
        try {
            imageController.pullImages();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
