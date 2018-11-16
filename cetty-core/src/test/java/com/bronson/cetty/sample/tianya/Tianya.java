package com.bronson.cetty.sample.tianya;

import com.bronson.cetty.core.Bootstrap;
import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.Result;
import com.bronson.cetty.core.Seed;
import com.bronson.cetty.core.handler.ConsoleReduceHandler;
import com.bronson.cetty.core.handler.HandlerContext;
import com.bronson.cetty.core.handler.ProcessHandlerAdapter;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * 抓取天涯论坛文章列表标题
 * http://bbs.tianya.cn/list-333-1.shtml
 *
 * @author heyingcai
 */
public class Tianya extends ProcessHandlerAdapter {

    @Override
    public void process(HandlerContext ctx, Page page) {
        //获取 Document
        Document document = page.getDocument();
        //dom解析
        Elements itemElements = document.
                select("div#bbsdoc>div#bd>div#main>div.mt5>table>tbody").
                get(2).
                select("tr");
        List<String> titles = Lists.newArrayList();
        for (Element item : itemElements) {
            String title = item.select("td.td-title").text();
            titles.add(title);
        }

        String href = document.select("div#bbsdoc>div#bd>div#main>div.short-pages-2>div.links>a[rel=nofollow]").first().attr("abs:href");

        //添加下一页的请求
        page.addNextSeed(new Seed(href));

        //添加下一页的请求
//        page.addNextSeed(new Seed("http://bbs.tianya.cn/list.jsp?item=333&nextid=1542249901000"));

        //获取Result对象，将我们解析出来的结果向下一个handler传递
        Result result = page.getResult();
        result.addResults(titles);

        //通过fireXXX 方法将本handler 处理的结果向下传递
        //本教程直接将结果传递给ConsoleHandler，将结果直接输出控制台
        ctx.fireReduce(page);
    }

    public static void main(String[] args) {

        //启动引导类
        Bootstrap.
                me().
                //使用同步抓取
                        isAsync(true).
                //开启一个线程
                        setThreadNum(1).
                //抓取入口url
                        startUrl("http://bbs.tianya.cn/list-333-1.shtml").
                //通用请求信息
                        setPayload(Payload.custom().setSleepTime(5000)).
                //添加自定处理器
                        addHandler(new Tianya()).
                //添加默认结果处理器，输出致控制台
                        addHandler(new ConsoleReduceHandler()).
                start();
    }
}
