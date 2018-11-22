package com.jibug.cetty.core.net;

import com.jibug.cetty.core.Payload;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author heyingcai
 */
public class SyncHttpClientGenerator extends AbstractHttpClientGenerator<CloseableHttpClient> {

    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    public SyncHttpClientGenerator() {
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry());
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100);
    }

    @Override
    public CloseableHttpClient getClient(Payload payload) {
        return build(payload);
    }

    @Override
    protected CloseableHttpClient build(Payload payload) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        if (payload.getUserAgent() != null) {
            httpClientBuilder.setUserAgent(payload.getUserAgent());
        } else {
            httpClientBuilder.setUserAgent("");
        }

        httpClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());

        SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
        socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
        socketConfigBuilder.setSoTimeout(payload.getSocketTimeout());
        SocketConfig socketConfig = socketConfigBuilder.build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(payload.getRetryTimes(), true));
        reduceCookie(httpClientBuilder, payload);
        return  httpClientBuilder.build();
    }

    @Override
    protected Registry<ConnectionSocketFactory> registry() {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSSLConnectionSocketFactory())
                .build();
    }

    private void reduceCookie(HttpClientBuilder httpClientBuilder, Payload payload) {
        if (payload.isUnsupportedCookie()) {
            httpClientBuilder.disableCookieManagement();
            return;
        }
        httpClientBuilder.setDefaultCookieStore(generatorCookie(payload));
    }

}
