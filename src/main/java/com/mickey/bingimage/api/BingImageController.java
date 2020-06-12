package com.mickey.bingimage.api;

import com.alibaba.fastjson.JSON;
import com.mickey.bingimage.common.HttpUtil;
import com.mickey.bingimage.dto.Image;
import com.mickey.bingimage.dto.JsonsRootBean;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * #Description
 *
 * @author wangmeng
 * @date 2020-06-12
 */
@RestController
@RequestMapping("/api/images")
public class BingImageController {

    private String baseUrl = "http://www.bing.com";
    private String jsonUrl = baseUrl + "/HPImageArchive.aspx?format=js&idx=0&n=10";
    @Value("${image.folder}")
    String folder;

    @GetMapping("")
    public JsonsRootBean getJson() throws IOException {
        HttpUtil.Response<JsonsRootBean> objectResponse = HttpUtil.get(jsonUrl, null, null);
        return JSON.parseObject(objectResponse.getResult(), JsonsRootBean.class);
    }

    @GetMapping("/auto")
    public List<Image> pullImages() throws IOException {
        JsonsRootBean jsonsRootBean = getJson();
        List<Image> result = new ArrayList<>();
        if (jsonsRootBean != null && jsonsRootBean.getImages() != null && !jsonsRootBean.getImages().isEmpty()) {
            jsonsRootBean.getImages().forEach(image -> {
                File file = FileUtils.getFile(folder + image.getEnddate() + ".jpg");
                if (!file.exists()) {
                    result.add(image);
                    try {
                        HttpUtil.Response inputStream = HttpUtil.get4InputStream(baseUrl + image.getUrl(), null, null);
                        FileUtils.copyInputStreamToFile(inputStream.getInputStream(), file);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                
            });
        }
        return result;
    }
}
