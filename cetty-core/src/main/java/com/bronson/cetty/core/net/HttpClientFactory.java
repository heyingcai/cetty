package com.bronson.cetty.core.net;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

/**
 * @author heyingcai
 */
public class HttpClientFactory {

    private static HttpClientGenerator<CloseableHttpAsyncClient> asyncHttpClientGenerator;

    private static HttpClientGenerator<CloseableHttpClient> httpClientHttpClientGenerator;

    public void initHttpclient(boolean async) {

    }
}
