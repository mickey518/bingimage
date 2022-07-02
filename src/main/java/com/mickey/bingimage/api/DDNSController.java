package com.mickey.bingimage.api;

import com.aliyuncs.utils.StringUtils;
import com.mickey.bingimage.service.DDNS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DDNS同步
 *
 * @author wangmeng
 */
@Slf4j
@RestController
@RequestMapping()
public class DDNSController {
    
    @Value("${aliyun.ddns.region-id}")
    private String regionId;
    @Value("${aliyun.ddns.access-key}")
    private String accessKey;
    @Value("${aliyun.ddns.access-key.secret}")
    private String accessKeySecret;
    @Value("${aliyun.ddns.sub-domain-list}")
    private String subDomainString;
    @Value("${aliyun.ddns.sub-domain-split-regex:,}")
    private String subDomainSplitRegex;
    
    @PostConstruct
    private void init() {
        log.info("程序启动，自动调用拉取最新的壁纸代码！！！");
        syncDns();
    }

    @GetMapping(value = {"sync", "/api/dns/sync"})
    public String syncDns() {
        List<String> subDomains = new ArrayList<>();
        if (!StringUtils.isEmpty(subDomainString)) {
            String[] strings = subDomainString.split(subDomainSplitRegex);
            subDomains = Arrays.stream(strings).collect(Collectors.toList());
        }
        return DDNS.sync(regionId, accessKey, accessKeySecret, subDomains);
    }
}
