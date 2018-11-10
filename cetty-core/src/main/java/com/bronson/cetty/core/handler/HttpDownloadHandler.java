package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Result;
import com.bronson.cetty.core.Seed;
import com.bronson.cetty.core.net.AsyncHttpClientGenerator;
import com.bronson.cetty.core.net.HttpClientGenerator;
import com.bronson.cetty.core.net.SyncHttpClientGenerator;
import com.bronson.cetty.core.scheduler.Scheduler;
import com.google.common.collect.Maps;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.util.Map;

/**
 * @author heyingcai
 */
public class HttpDownloadHandler extends ProcessHandlerAdapter {

    private final Map<String, CloseableHttpClient> httpClients = Maps.newHashMap();

    private final Map<String, CloseableHttpAsyncClient> httpAsyncClients = Maps.newHashMap();

    HttpClientGenerator<CloseableHttpAsyncClient> asyncHttpClientGenerator = new AsyncHttpClientGenerator();

    HttpClientGenerator<CloseableHttpClient> httpClientHttpClientGenerator = new SyncHttpClientGenerator();

    @Override
    public void download(HandlerContext ctx, Seed seed, Scheduler scheduler, boolean async) {
        Result result = new Result();
        Page page = new Page();
        page.setResult(result);
        ctx.fireProcess(page);
    }

}
