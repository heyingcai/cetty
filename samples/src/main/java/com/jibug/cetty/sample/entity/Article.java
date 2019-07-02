package com.jibug.cetty.sample.entity;

/**
 * @author heyingcai
 */
public class Article {

    private int id;

    private String title;

    private String summary;

    private String publishTime;

    private String url;

    private String listPhoto;

    private String content;

    private String via;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getListPhoto() {
        return listPhoto;
    }

    public void setListPhoto(String listPhoto) {
        this.listPhoto = listPhoto;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", url='" + url + '\'' +
                ", listPhoto='" + listPhoto + '\'' +
                ", content='" + content + '\'' +
                ", via='" + via + '\'' +
                '}';
    }
}
