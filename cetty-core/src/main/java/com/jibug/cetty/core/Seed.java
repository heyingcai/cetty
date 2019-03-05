package com.jibug.cetty.core;

import com.jibug.cetty.core.model.RequestBody;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * @author heyingcai
 */
public class Seed implements Serializable {

    private String url;

    private String method;

    private RequestBody requestBody;

    private Map<String, String> cookies = Maps.newHashMap();

    private Map<String, String> headers = Maps.newHashMap();

    private String charset;

    /**
     * in one request, there perhaps have some attach information for pass to next handler
     */
    private Map<String, Object> attach;

    public Seed(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Seed setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public Seed addCookie(String key, String value) {
        cookies.put(key, value);
        return this;
    }

    public Seed addCookies(Map<String, String> cookieMap) {
        cookies.putAll(cookieMap);
        return this;
    }

    public Seed addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Seed addHeaders(Map<String, String> headerMap) {
        headers.putAll(headerMap);
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Seed setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public Seed putAttach(String key, String value) {
        if (attach == null) {
            attach = Maps.newHashMap();
        }
        attach.put(key, value);
        return this;
    }

    public Seed putAttach(String key, Object value) {
        if (attach == null) {
            attach = Maps.newHashMap();
        }
        attach.put(key, value);
        return this;
    }

    public Object getAttach(String key) {
        if (attach == null) {
            return null;
        }
        return attach.get(key);
    }

    public Map<String, Object> getAttach() {
        return attach;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Seed seed = (Seed) o;

        if (url != null ? !url.equals(seed.url) : seed.url != null) {
            return false;
        }
        return method != null ? method.equals(seed.method) : seed.method == null;
    }

    @Override
    public String toString() {
        return "Seed{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", requestBody=" + requestBody +
                ", cookies=" + cookies +
                ", headers=" + headers +
                ", charset='" + charset + '\'' +
                ", attach=" + attach +
                '}';
    }

}
