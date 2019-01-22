package com.jibug.cetty.core;

import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.util.List;
import java.util.Map;

/**
 * @author heyingcai
 */
public class Page {

    private String url;

    private Seed seed;

    private Result result = new Result();

    private String rawData;

    private byte[] bytes;

    private List<Seed> nextSeeds = Lists.newLinkedList();

    private Map<String,List<String>> headers;

    private Html html;

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
            this.html = new Html(Jsoup.parse(text, url));
        } catch (Exception e) {
            this.html = new Html(null);
        }
    }

    public Document getDocument() {
        return html.document;
    }

    public Html getHtml() {
        return html;
    }

    public class Html {

        private JXDocument jxDocument;

        private Document document;

        Html(Document document) {
            this.document = document;
            this.jxDocument = JXDocument.create(document);
        }

        public List<Object> select(String xpath) {
            return html.jxDocument.sel(xpath);
        }

        public Object selectOne(String xpath) {
            return html.jxDocument.selOne(xpath);
        }

        public List<JXNode> selectNode(String xpath) {
            return html.jxDocument.selN(xpath);
        }

        public JXNode selectOneNode(String xpath) {
            return html.jxDocument.selNOne(xpath);
        }
    }

}
