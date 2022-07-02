package com.mickey.bingimage.api;

import com.mickey.bingimage.service.DDNS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * DDNS同步
 *
 * @author wangmeng
 */
@Slf4j
@RestController
@RequestMapping()
public class DDNSController {
    @PostConstruct
    private void init() {
        log.info("程序启动，自动调用拉取最新的壁纸代码！！！");
        syncDns();
    }

    @GetMapping(value = {"sync", "/api/dns/sync"})
    public String syncDns() {
        return DDNS.sync();
    }
}
