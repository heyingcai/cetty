package com.jibug.cetty.core.bootstrap;

import com.jibug.cetty.core.Bootstrap;
import com.jibug.cetty.core.Payload;
import com.jibug.cetty.core.handler.PageProcessHandler;

/**
 * @author heyingcai
 */
public class BootstrapTest {

    public static void main(String[] args) {
        //启动引导类
        Bootstrap.
                me().
                //是否异步抓取
                isAsync(true).
                //开启线程数
                setThreadNum(1).
                //起始url
                startUrl("http://www.baidu.com").
                //全局请求信息
                setPayload(Payload.custom()).
                //自定处理器handler
                addHandler(new PageProcessHandler()).
                //启动
                start();
    }
}
