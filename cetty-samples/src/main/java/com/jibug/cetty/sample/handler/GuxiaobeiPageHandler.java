package com.jibug.cetty.sample.handler;

import com.jibug.cetty.core.Page;
import com.jibug.cetty.core.Seed;
import com.jibug.cetty.core.handler.HandlerContext;
import com.jibug.cetty.sample.entity.Article;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抓取顾小北博客
 * http://www.guxiaobei.com/shopify
 *
 * @author heyingcai
 */
@Component
public class GuxiaobeiPageHandler extends BasePageHandler {

    private static final Pattern PAGE_REGEX_PATTERN = Pattern.compile("/page/(\\d+)");

    @Override
    public void process(HandlerContext ctx, Page page) {
        parseRoute(ctx, page);
    }

    @Override
    protected void parseRoute(HandlerContext ctx, Page page) {
        String pageUrl = page.getUrl();

        if (pageUrl.contains("/page/")) {
            parseListing(ctx, page);
        } else {
            parseBody(ctx, page);
        }
    }

    /**
     * 解析文章列表
     *
     * @param page
     */
    @Override
    public void parseListing(HandlerContext ctx, Page page) {
        Document document = page.getDocument();

        Elements articles = document.select("article.excerpt");

        List<Seed> seeds = new ArrayList<>();
        for (Element article : articles) {
            Elements header = article.select("header>h2>a");
            String url = header.attr("href");
            String title = header.attr("title");

            Elements listPhoto = article.select("div.focus>a>img");
            String listPhotoUrl = listPhoto.attr("src");

            String summary = article.select("span.note").first().ownText();

            Seed seed = new Seed(url);
            seed.putAttach("via", page.getSeed().getAttach("via"));
            seed.putAttach("listPhoto", listPhotoUrl);
            seed.putAttach("summary", summary);
            seed.putAttach("title", title);

            seeds.add(seed);
        }

        Matcher matcher = PAGE_REGEX_PATTERN.matcher(page.getUrl());
        if (!matcher.find()) {
            return;
        }

        final String pageNumStr = matcher.group(0).replace("/page/", "");
        int nextPageNum = Integer.parseInt(pageNumStr);
        int pageLimit = Integer.parseInt(page.getSeed().getAttach("pageLimit").toString());

        if (++nextPageNum <= pageLimit) {
            String nextPageUrl = String.format("http://www.guxiaobei.com/search/shopify/page/%d", nextPageNum);
            Seed seed = new Seed(nextPageUrl);
            seed.putAttach("pageLimit", page.getSeed().getAttach("pageLimit").toString());
            seed.putAttach("via", page.getSeed().getAttach("via").toString());
            page.addNextSeed(seed);
        }

        page.addNextSeed(seeds);

        ctx.fireReduce(page);
    }

    /**
     * 解析文章内容
     *
     * @param page
     */
    @Override
    public void parseBody(HandlerContext ctx, Page page) {
        Document document = page.getDocument();

        String publishTime = "";
        try {
            publishTime = dealDateFormat(document.select("meta[property=article:published_time]").attr("content"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Article article = new Article();
        article.setPublishTime(publishTime);

        Elements content = document.select("article.article-content>*");

        buildArticle(article, page, appendBody(content));

        page.getResult().putField("article", article);

        ctx.fireReduce(page);
    }

    @Override
    public Element appendBody(Elements tempBody) {
        final Element articleBody = new Element(Tag.valueOf("div"), "");
        for (final Element pEl : tempBody) {
            if (pEl.select("div.open-message,div.jp-relatedposts,div.article-social").size() != 0) {
                continue;
            }
            if (pEl.tagName().equals("p")) {
                Element imgEl = pEl.select("img").first();
                if (imgEl != null) {
                    String src = imgEl.attr("src");
                    if (src.contains("data:image")) {
                        src = imgEl.attr("data-src");
                    } else if (!src.contains("www.guxiaobei.com")) {
                        src = "http://www.guxiaobei.com" + src;
                    }
                    imgEl.attr("src", src);

                    articleBody.appendChild(buildFigure(imgEl));
                    continue;
                }
            }
            articleBody.appendChild(pEl);
        }
        return articleBody;
    }
}
