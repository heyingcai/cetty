package com.jibug.cetty.core.net;

import com.jibug.cetty.core.Payload;
import com.jibug.cetty.core.constants.HttpConstants;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author heyingcai
 */
public abstract class AbstractHttpClientGenerator<T> implements HttpClientGenerator<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpClientGenerator.class);

    /**
     * building httpclient
     *
     * @param payload
     * @return
     */
    protected abstract T build(Payload payload);

    /**
     * RegistryBuilder construct
     *
     * @return
     */
    protected abstract Registry<?> registry();

    public AbstractHttpClientGenerator() {
    }

    protected SSLIOSessionStrategy buildSSLIOSessionStrategy() {
        SSLContext sslcontext = SSLContexts.createDefault();
        return new SSLIOSessionStrategy(sslcontext);
    }

    protected SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
            return new SSLConnectionSocketFactory(createIgnoreVerifySSL(), new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    new DefaultHostnameVerifier());
        } catch (KeyManagementException e) {
            logger.error("ssl connection fail", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("ssl connection fail", e);
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        SSLContext sc = SSLContext.getInstance("SSLv3");
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    public class CustomRedirectStrategy extends LaxRedirectStrategy {

        @Override
        public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
            URI uri = getLocationURI(request, response, context);
            String method = request.getRequestLine().getMethod();
            if (HttpConstants.POST.equalsIgnoreCase(method)) {
                try {
                    HttpRequestWrapper httpRequestWrapper = (HttpRequestWrapper) request;
                    httpRequestWrapper.setURI(uri);
                    httpRequestWrapper.removeHeaders("Content-Length");
                    return httpRequestWrapper;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new HttpPost(uri);
            } else {
                return new HttpGet(uri);
            }
        }
    }

    protected CookieStore generatorCookie(Payload payload) {
        CookieStore cookieStore = new BasicCookieStore();
        if (!payload.getOriginCookies().isEmpty()) {
            for (Map.Entry<String, String> cookieEntry : payload.getOriginCookies().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie.setDomain(payload.getDomain());
                cookieStore.addCookie(cookie);
            }
        }
        if (!payload.getCookies().isEmpty()) {
            for (Map.Entry<String, Map<String, String>> domainEntry : payload.getCookies().entrySet()) {
                for (Map.Entry<String, String> cookieEntry : domainEntry.getValue().entrySet()) {
                    BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                    cookie.setDomain(domainEntry.getKey());
                    cookieStore.addCookie(cookie);
                }
            }
        }
        return cookieStore;
    }

}
