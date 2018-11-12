package com.bronson.cetty.core.model;

import com.google.common.collect.Lists;
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
public class RequestBody implements Serializable {

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
