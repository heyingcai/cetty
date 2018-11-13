package com.bronson.cetty.core;

import com.bronson.cetty.core.net.Proxy;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * http request payload
 *
 * @author heyingcai
 */
public class Payload {

    /**
     * set domain
     */
    private String domain;

    /**
     * set ua
     */
    private String userAgent;

    /**
     * set charset
     */
    private String charset = "utf-8";

    /**
     * set proxy
     */
    private Proxy proxy;

    /**
     * set origin cookies
     */
    private Map<String, String> originCookies = Maps.newHashMap();

    /**
     * set specific cookies
     */
    private Map<String, Map<String, String>> cookies = Maps.newHashMap();

    /**
     * set http request headers
     */
    private Map<String, String> headers = Maps.newHashMap();

    /**
     * set socket timeout
     */
    private int socketTimeout = 5000;

    /**
     * set connect timeout
     */
    private int connectTimeout = 2000;

    /**
     * set connection default pool capacity
     */
    private int connectionPoolCapacity = 5;

    /**
     * set request retryTimes
     */
    private int retryTimes = 0;

    /**
     * sometime there unsupported cookie
     */
    private boolean unsupportedCookie = false;

    /**
     * return a new payload instance
     *
     * @return
     */
    public static Payload custom() {
        return new Payload();
    }

    public Payload setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public String getCharset() {
        return charset;
    }

    public Payload setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public Payload setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Payload setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public Payload addOriginCookie(String name, String value) {
        originCookies.put(name, value);
        return this;
    }

    public Map<String, String> getOriginCookies() {
        return originCookies;
    }

    public Payload addOriginCookies(Map<String, String> cookies) {
        originCookies.putAll(cookies);
        return this;
    }

    public Payload addCookie(String domain, String name, String value) {
        if (!cookies.containsKey(domain)) {
            HashMap<String, String> cookie = Maps.newHashMap();
            cookies.put(domain, cookie);
        }
        cookies.get(domain).put(name, value);
        return this;
    }

    public Payload addCookies(Map<String, Map<String, String>> cookieMap) {
        for (Map.Entry<String, Map<String, String>> cookie : cookieMap.entrySet()) {
            if (!cookies.containsKey(cookie.getKey())) {
                cookies.putAll(cookies);
            }
        }
        return this;
    }

    public Map<String, Map<String, String>> getCookies() {
        return cookies;
    }

    public Payload addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Payload addHeaders(Map<String, String> headerMap) {
        headers.putAll(headerMap);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Payload setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public Payload setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public Payload setConnectionPoolCapacity(int connectionPoolCapacity) {
        this.connectionPoolCapacity = connectionPoolCapacity;
        return this;
    }

    public int getConnectionPoolCapacity() {
        return connectionPoolCapacity;
    }

    public Payload setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Payload setUnsupportedCookie(boolean unsupportedCookie) {
        this.unsupportedCookie = unsupportedCookie;
        return this;
    }

    public boolean isUnsupportedCookie() {
        return unsupportedCookie;
    }
}
