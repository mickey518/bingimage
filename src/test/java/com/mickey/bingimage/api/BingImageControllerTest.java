package com.mickey.bingimage.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


/**
 * description
 *
 * @author wangmeng
 * @version 1.0.0
 * @ClassName BingImageControllerTest.java
 * @Description TODO
 * @createTime 2020年06月17日 18:36:00
 */
@SpringBootTest
class BingImageControllerTest {

    @Autowired
    private BingImageController controller;

    @Test
    void getJson() throws IOException {
        System.out.println(JSON.toJSONString(controller.getJson(), SerializerFeature.PrettyFormat));
    }
}