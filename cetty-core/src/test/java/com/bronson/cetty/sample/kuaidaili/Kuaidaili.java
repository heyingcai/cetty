package com.bronson.cetty.sample.kuaidaili;

import com.bronson.cetty.core.Bootstrap;
import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.Result;
import com.bronson.cetty.core.handler.ConsoleReduceHandler;
import com.bronson.cetty.core.handler.HandlerContext;
import com.bronson.cetty.core.handler.ProcessHandlerAdapter;
import com.bronson.cetty.core.net.Proxy;
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

        Elements elements = document.select("div#content>div.con-body>div>div#list>table>tbody>tr");

        List<Proxy> proxies = Lists.newArrayList();
        for (Element element : elements) {
            String ip = element.select("td[data-title=IP]").text();
            String port = element.select("td[data-title=PORT]").text();
            String scheme = element.select("td[data-title=类型]").text();
            Proxy proxy = new Proxy(ip, Integer.parseInt(port),scheme);
            proxies.add(proxy);
        }

        page.getResult().addResults(proxies);

        ctx.fireReduce(page);
    }

    public static void main(String[] args) {
        Bootstrap.me().
                startUrl("https://www.kuaidaili.com/free").
                addHandler(new Kuaidaili()).setThreadNum(1).
                addHandler(new ConsoleReduceHandler()).
                setPayload(Payload.custom()).
                isAsync(false).
                start();
    }
}
