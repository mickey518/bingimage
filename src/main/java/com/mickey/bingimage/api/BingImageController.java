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
 */
@RestController
@RequestMapping()
public class BingImageController {
	
	private final String baseUrl = "http://www.bing.com";
	@Value("${image.folder}")
	private       String folder;
	
	@GetMapping("/api/images")
	public JsonsRootBean getJson() throws IOException {
		String jsonUrl = baseUrl + "/HPImageArchive.aspx?format=js&idx=0&n=10";
		HttpUtil.Response<JsonsRootBean> objectResponse = HttpUtil.get(jsonUrl, null, null);
		JsonsRootBean bean;
		try {
			bean = JSON.parseObject(objectResponse.getResult(), JsonsRootBean.class);
		} catch (Exception e) {
			System.out.println(objectResponse.getResult());
			throw e;
		}
		return bean;
	}
	
	@GetMapping(value = {"", "/api/images/auto"})
	public List<Image> pullImages() throws IOException {
		JsonsRootBean jsonsRootBean = getJson();
		List<Image> result = new ArrayList<>();
		if (jsonsRootBean != null && jsonsRootBean.getImages() != null && !jsonsRootBean.getImages().isEmpty()) {
			if (!folder.endsWith(File.separator)) {
				folder = folder + File.separator;
			}
			jsonsRootBean.getImages().forEach(image -> {
				String filePath = folder + image.getEnddate() + ".jpg";
				System.out.println(filePath);
				File file = FileUtils.getFile(filePath);
				if (!file.exists()) {
					System.out.println("image not existed, downloading...");
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
