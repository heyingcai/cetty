package com.jibug.cetty.sample.runner;

import com.jibug.cetty.sample.service.CrawlerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author heyingcai
 * @date 2019-07-02 17:53
 */
@Component
public class CrawlerRunner implements CommandLineRunner {

    @Resource
    private CrawlerService crawlerService;

    @Override
    public void run(String... args) throws Exception {
        crawlerService.start();
    }
}
