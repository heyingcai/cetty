package com.jibug.cetty.sample.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jibug.cetty.core.Bootstrap;
import com.jibug.cetty.core.Payload;
import com.jibug.cetty.core.Seed;
import com.jibug.cetty.core.handler.ProcessHandlerAdapter;
import com.jibug.cetty.core.handler.ReduceHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author heyingcai
 * @date 2019-07-02 17:43
 */
@Component
public class CrawlerService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    @Resource
    private TaskService taskService;

    @Resource
    private ApplicationContext applicationContext;

    public void start() {
        JSONArray taskObject = taskService.getTaskObject();

        if (taskObject.size() != 0) {
            logger.info("获取到待抓取任务 {}", taskObject.toJSONString());
            for (int i = 0; i < taskObject.size(); i++) {
                JSONObject task = taskObject.getJSONObject(i);
                if (task.getInteger("status") == 0) {
                    continue;
                }

                Seed seed = new Seed(task.getString("url"));
                seed.putAttach("via", task.getString("via"));
                seed.putAttach("pageLimit", task.getString("pageLimit"));

                //定义爬虫引导程序
                Bootstrap bootstrap = Bootstrap.me()
                        .startSeed(seed)
                        .addHandler(applicationContext.getBean(task.getString("pageHandler"), ProcessHandlerAdapter.class))
                        .addHandler(applicationContext.getBean(task.getString("pageReducer"), ReduceHandlerAdapter.class))
                        .setThreadNum(1)
                        .setPayload(Payload.custom()
                                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36"))
                        .isAsync(false);

                bootstrap.start();
            }
        }


    }

}
