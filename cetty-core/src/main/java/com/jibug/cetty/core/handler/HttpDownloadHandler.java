package com.jibug.cetty.core.handler;

import com.jibug.cetty.core.Page;
import com.jibug.cetty.core.Payload;
import com.jibug.cetty.core.Seed;
import com.jibug.cetty.core.constants.HttpConstants;
import com.jibug.cetty.core.net.Proxy;
import com.jibug.cetty.core.utils.UrlUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heyingcai
 */
public class HttpDownloadHandler extends ProcessHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpDownloadHandler.class);

    @Override
    public void download(HandlerContext ctx, Seed seed) {
        Payload payload = ctx.cetty().getPayload();
        if (payload == null) {
            throw new NullPointerException("payload can not be null");
        }
        //get cetty state
        boolean async = ctx.cetty().isAsync();
        if (async) {
            asyncHttpClientDownload(ctx, seed);
        } else {
            httpClientDownload(ctx, seed);
        }
    }

    private void httpClientDownload(HandlerContext ctx, Seed seed) {
        Payload payload = ctx.cetty().getPayload();

        CloseableHttpClient httpClient = ctx.cetty().getHttpClient();
        CloseableHttpResponse httpResponse = null;

        Page page;
        try {
            httpResponse = httpClient.execute(convertHttpUriRequest(seed, payload), convertHttpClientContext(seed, payload));
            page = handleResponse(seed, seed.getCharset() != null ? seed.getCharset() : payload.getCharset(), httpResponse);
            logger.info("download {} page success !", seed.getUrl());
            ctx.fireProcess(page);
        } catch (IOException e) {
            logger.warn("download {} page error !", seed.getUrl(), e);
        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }

    }

    private void asyncHttpClientDownload(HandlerContext ctx, Seed seed) {
        Payload payload = ctx.cetty().getPayload();
        CloseableHttpAsyncClient httpAsyncClient = ctx.cetty().getHttpAsyncClient();

        try {
            httpAsyncClient.execute(convertHttpUriRequest(seed, payload), convertHttpClientContext(seed, payload), new CallBack(seed, ctx, payload));
        } catch (Exception e) {
            logger.warn("download {} page error !", seed.getUrl(), e);
        }
    }

    class CallBack implements FutureCallback<HttpResponse> {

        private final Seed seed;
        private final HandlerContext ctx;
        private final Payload payload;

        public CallBack(Seed seed, HandlerContext ctx, Payload payload) {
            this.seed = seed;
            this.ctx = ctx;
            this.payload = payload;
        }

        @Override
        public void completed(HttpResponse httpResponse) {
            try {
                Page page = handleResponse(seed, seed.getCharset() != null ? seed.getCharset() : payload.getCharset(), httpResponse);
                logger.info("download {} page success !", seed.getUrl());
                ctx.fireProcess(page);
            } catch (IOException e) {
                logger.warn("download {} page error !", seed.getUrl(), e);
            } finally {
                if (httpResponse != null) {
                    EntityUtils.consumeQuietly(httpResponse.getEntity());
                }
            }
        }

        @Override
        public void failed(Exception e) {
            logger.warn("download {} page error !", seed.getUrl(), e);
        }

        @Override
        public void cancelled() {
            logger.warn("download {} page cancelled", seed.getUrl());
        }
    }


    private Page handleResponse(Seed seed, String charset, HttpResponse httpResponse) throws IOException {
        byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        Page page = new Page();
        page.setBytes(bytes);
        page.setRawData(new String(bytes, charset));
        page.setUrl(seed.getUrl());
        page.setSeed(seed);
        page.setHeaders(convertHeaders(httpResponse.getAllHeaders()));
        page.setDocument(new String(bytes, charset), seed.getUrl());
        return page;
    }

    private Map<String, List<String>> convertHeaders(Header[] headers) {
        Map<String, List<String>> results = new HashMap<>();
        for (Header header : headers) {
            List<String> list = results.get(header.getName());
            if (list == null) {
                list = new ArrayList<>();
                results.put(header.getName(), list);
            }
            list.add(header.getValue());
        }
        return results;
    }

    private HttpClientContext convertHttpClientContext(Seed seed, Payload payload) {
        Proxy proxy = payload.getProxy();
        HttpClientContext httpContext = new HttpClientContext();
        if (proxy != null && proxy.getUsername() != null) {
            AuthState authState = new AuthState();
            authState.update(new BasicScheme(ChallengeState.PROXY), new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword()));
            httpContext.setAttribute(HttpClientContext.PROXY_AUTH_STATE, authState);
        }
        if (payload.getCookies() != null && !payload.getCookies().isEmpty()) {
            CookieStore cookieStore = new BasicCookieStore();
            Map<String, Map<String, String>> cookies = payload.getCookies();
            for (Map.Entry<String, Map<String, String>> cookieEntry : cookies.entrySet()) {
                Map<String, String> value = cookieEntry.getValue();
                for (Map.Entry<String, String> entry : value.entrySet()) {
                    BasicClientCookie cookie1 = new BasicClientCookie(entry.getKey(), entry.getValue());
                    cookie1.setDomain(cookieEntry.getKey());
                    cookieStore.addCookie(cookie1);
                }
            }
            httpContext.setCookieStore(cookieStore);
        }

        if (!seed.getCookies().isEmpty()) {
            CookieStore cookieStore = new BasicCookieStore();
            Map<String, String> cookies = seed.getCookies();
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                BasicClientCookie cookie1 = new BasicClientCookie(cookie.getKey(), cookie.getValue());
                cookie1.setDomain(UrlUtils.getDomain(seed.getUrl())
                );
                cookieStore.addCookie(cookie1);
            }
            httpContext.setCookieStore(cookieStore);
        }
        return httpContext;
    }

    private HttpUriRequest convertHttpUriRequest(Seed seed, Payload payload) {
        RequestBuilder requestBuilder = getRequestMethod(seed).setUri(seed.getUrl());
        if (payload.getHeaders() != null) {
            for (Map.Entry<String, String> headerEntry : payload.getHeaders().entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        if (!seed.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> headerEntry : seed.getHeaders().entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        if (payload != null) {
            requestConfigBuilder.setConnectionRequestTimeout(payload.getConnectTimeout())
                    .setSocketTimeout(payload.getSocketTimeout())
                    .setConnectTimeout(payload.getConnectTimeout())
                    .setCookieSpec(CookieSpecs.STANDARD);
        }

        if (payload.getProxy() != null) {
            requestConfigBuilder.setProxy(new HttpHost(payload.getProxy().getHost(), payload.getProxy().getPort()));
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        HttpUriRequest httpUriRequest = requestBuilder.build();
        return httpUriRequest;
    }

    private RequestBuilder getRequestMethod(Seed seed) {
        String method = seed.getMethod();
        if (method == null || method.equalsIgnoreCase(HttpConstants.GET)) {
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase(HttpConstants.POST)) {
            return addFormParams(RequestBuilder.post(), seed);
        } else if (method.equalsIgnoreCase(HttpConstants.HEAD)) {
            return RequestBuilder.head();
        } else if (method.equalsIgnoreCase(HttpConstants.PUT)) {
            return addFormParams(RequestBuilder.put(), seed);
        } else if (method.equalsIgnoreCase(HttpConstants.DELETE)) {
            return RequestBuilder.delete();
        } else if (method.equalsIgnoreCase(HttpConstants.TRACE)) {
            return RequestBuilder.trace();
        }
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

    private RequestBuilder addFormParams(RequestBuilder requestBuilder, Seed seed) {
        if (seed.getRequestBody() != null) {
            ByteArrayEntity entity = new ByteArrayEntity(seed.getRequestBody().getBody());
            entity.setContentType(seed.getRequestBody().getContentType());
            requestBuilder.setEntity(entity);
        }
        return requestBuilder;
    }

}
