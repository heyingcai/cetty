package com.bronson.cetty.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
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

    public Object getAttch(String key) {
        if (attach == null) {
            return null;
        }
        return attach.get(key);
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

    static final class RequestBody implements Serializable {

        private byte[] body;
        private String contentType;
        private String charsetName;

        enum ContentType {
            /**
             * content-type: json
             */
            JSON("application/json"),
            /**
             * content-type:form
             */
            FORM("application/x-www-form-urlencoded"),
            /**
             * content-type:xml
             */
            XML("text/xml");

            private String type;

            ContentType(String type) {
                this.type = type;
            }

            public String getType() {
                return type;
            }
        }

        public RequestBody(byte[] body, String contentType, String charsetName) {
            this.body = body;
            this.contentType = contentType;
            this.charsetName = charsetName;
        }

        public byte[] getBody() {
            return body;
        }

        public void setBody(byte[] body) {
            this.body = body;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getCharsetName() {
            return charsetName;
        }

        public void setCharsetName(String charsetName) {
            this.charsetName = charsetName;
        }

        public static RequestBody custom(byte[] body, String contentType, String charsetName) {
            return new RequestBody(body, contentType, charsetName);
        }

        public static RequestBody toJson(String json, String charsetName) {
            try {
                return new RequestBody(json.getBytes(charsetName), ContentType.JSON.getType(), charsetName);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("illegal charset " + charsetName, e);
            }
        }

        public static RequestBody toForm(Map<String, Object> params, String charsetName) {
            List<NameValuePair> nameValuePairs = Lists.newArrayListWithCapacity(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
            try {
                return new RequestBody(URLEncodedUtils.format(nameValuePairs, charsetName).getBytes(charsetName), ContentType.FORM.getType(), charsetName);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("illegal charset " + charsetName, e);
            }
        }

        public static RequestBody toXml(String xml, String charsetName) {
            try {
                return new RequestBody(xml.getBytes(charsetName), ContentType.XML.getType(), charsetName);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("illegal charset " + charsetName, e);
            }
        }

    }

}
