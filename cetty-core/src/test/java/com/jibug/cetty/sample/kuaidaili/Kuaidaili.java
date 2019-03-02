package com.jibug.cetty.sample.kuaidaili;

import com.jibug.cetty.core.Bootstrap;
import com.jibug.cetty.core.Page;
import com.jibug.cetty.core.Payload;
import com.jibug.cetty.core.handler.ConsoleReduceHandler;
import com.jibug.cetty.core.handler.HandlerContext;
import com.jibug.cetty.core.handler.ProcessHandlerAdapter;
import com.jibug.cetty.core.net.Proxy;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * @author heyingcai
 */
public class Kuaidaili extends ProcessHandlerAdapter {

    @Override
    public void process(HandlerContext ctx, Page page) {
        Document document = page.getDocument();

        //jsoup
        Elements elements = document.select("div#content>div.con-body>div>div#list>table>tbody>tr");

        //xpath
        List<Object> select = page.getHtml().select("//div[@id='content']/div[@class='con-body']/div/div[@id='list']/table/tbody/tr/td[@data-title='IP']/text()");

        List<Proxy> proxies = Lists.newArrayList();
        for (Element element : elements) {
            String ip = element.select("td[data-title=IP]").text();
            String port = element.select("td[data-title=PORT]").text();
            String scheme = element.select("td[data-title=类型]").text();
            Proxy proxy = new Proxy(ip, Integer.parseInt(port),scheme);
            proxies.add(proxy);
        }

        System.out.println(select);

        page.getResult().addResults(proxies);

        ctx.fireReduce(page);
    }

    public static void main(String[] args) throws InterruptedException {
        Bootstrap async = Bootstrap.me().
                startUrl("https://www.kuaidaili.com/free").
                addHandler(new Kuaidaili()).setThreadNum(1).
                addHandler(new ConsoleReduceHandler()).
                setPayload(Payload.custom()).
                isAsync(false);

        async.start();

        Thread.sleep(15000);

        async.stop();
    }
}
