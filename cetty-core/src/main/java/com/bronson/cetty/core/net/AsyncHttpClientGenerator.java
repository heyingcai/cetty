package com.bronson.cetty.core.net;

import com.bronson.cetty.core.Payload;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.nio.charset.CodingErrorAction;

/**
 * @author heyingcai
 */
public class AsyncHttpClientGenerator extends AbstractHttpClientGenerator<CloseableHttpAsyncClient> {

    private PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager;

    public AsyncHttpClientGenerator() {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .build();
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        poolingNHttpClientConnectionManager = new PoolingNHttpClientConnectionManager(ioReactor, null, registry(), null);
        poolingNHttpClientConnectionManager.setDefaultMaxPerRoute(100);
    }

    @Override
    public CloseableHttpAsyncClient getClient(Payload payload) {
        return build(payload);
    }

    @Override
    protected CloseableHttpAsyncClient build(Payload payload) {
        HttpAsyncClientBuilder asyncClientBuilder = HttpAsyncClients.custom();

        if (StringUtils.isNotBlank(payload.getUserAgent())) {
            asyncClientBuilder.setUserAgent(payload.getUserAgent());
        } else {
            asyncClientBuilder.setUserAgent("");
        }

        asyncClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(payload.getConnectTimeout())
                .setSocketTimeout(payload.getSocketTimeout()).build();

        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8).build();

        poolingNHttpClientConnectionManager.setDefaultConnectionConfig(connectionConfig);
        asyncClientBuilder.setConnectionManager(poolingNHttpClientConnectionManager);
        asyncClientBuilder.setDefaultRequestConfig(requestConfig);
        if (payload.getProxy() != null) {
            Proxy proxy = payload.getProxy();
            HttpHost httpHost = new HttpHost(proxy.getHost(), proxy.getPort(), proxy.getScheme());
            asyncClientBuilder.setProxy(httpHost);
        }
        reduceCookie(asyncClientBuilder,payload);
        return asyncClientBuilder.build();
    }

    @Override
    protected Registry<SchemeIOSessionStrategy> registry() {
        return RegistryBuilder
                .<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", buildSSLIOSessionStrategy())
                .build();
    }

    private void reduceCookie(HttpAsyncClientBuilder asyncClientBuilder, Payload payload) {
        if (payload.isUnsupportedCookie()) {
            asyncClientBuilder.disableCookieManagement();
            return;
        }
        asyncClientBuilder.setDefaultCookieStore(generatorCookie(payload));
    }

}
