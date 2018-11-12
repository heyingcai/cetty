package com.bronson.cetty.core;

import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

/**
 * @author heyingcai
 */
public class Page {

    private String url;

    private Seed seed;

    private Result result;

    private String rawData;

    private byte[] bytes;

    private List<Seed> nextSeeds = Lists.newLinkedList();

    private Map<String,List<String>> headers;

    private Document document;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    public Result getResult() {
        result.setSeed(seed);
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void addNextSeed(Seed seed) {
        nextSeeds.add(seed);
    }

    public void addNextSeed(String url) {
        nextSeeds.add(new Seed(url));
    }

    public void addNextSeed(List<Seed> seeds) {
        nextSeeds.addAll(seeds);
    }

    public List<Seed> getNextSeeds() {
        return nextSeeds;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setNextSeeds(List<Seed> nextSeeds) {
        this.nextSeeds = nextSeeds;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public void setDocument(String text, String url) {
        try {
            this.document = Jsoup.parse(text, url);
        } catch (Exception e) {
            this.document = null;
        }
    }

    public Document getDocument() {
        return document;
    }
}
