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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * waimaob2c博客抓取
 * https://www.waimaob2c.com/
 *
 * @author heyingcai
 */
@Component
public class Waimaob2cPageHandler extends BasePageHandler {

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

    @Override
    protected void parseListing(HandlerContext ctx, Page page) {
        Document document = page.getDocument();

        Elements articles = document.select("article.excerpt");
        List<Seed> seeds = new ArrayList<>();
        for (Element article : articles) {
            String tagName = article.select("header>a").first().ownText();
            if (tagName.contains("资讯动态")) {
                continue;
            }

            Element header = article.select("header").first();
            String title = header.select("h2>a").first().ownText();
            String url = header.select("h2>a").attr("href");

            Seed seed = new Seed(url);
            seed.putAttach("title", title);
            Elements listPhoto = article.select("p.focus");
            if (listPhoto != null) {
                String listPhotoUrl = listPhoto.select("a>img,a>span>img").attr("data-original");
                seed.putAttach("listPhoto", listPhotoUrl);
            }

            String summary = article.select("p.note").text();
            seed.putAttach("summary", summary);
            seed.putAttach("via", page.getSeed().getAttach("via"));

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
            String nextPageUrl = String.format("https://www.waimaob2c.com/page/%d", nextPageNum);
            Seed seed = new Seed(nextPageUrl);
            seed.putAttach("pageLimit", page.getSeed().getAttach("pageLimit").toString());
            seed.putAttach("via", page.getSeed().getAttach("via").toString());
            page.addNextSeed(seed);
        }

        page.addNextSeed(seeds);

        ctx.fireReduce(page);
    }

    @Override
    protected void parseBody(HandlerContext ctx, Page page) {
        Document document = page.getDocument();

        Element metaEl = document.select("header.article-header>ul.article-meta>li").first();
        String publishTime = metaEl.ownText().replace("Shopify 发布于 ", "");

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
        for (Element body : tempBody) {
            if (body.tagName().equals("p")) {
                boolean skipRegister = body.select("p").text().contains("即刻注册SHOPIFY账户, 跟着我们精心准备的SHOPIFY教程开始外贸独立站之旅!");
                boolean skipCopyRight = body.classNames().contains("post-copyright");
                if (skipRegister || skipCopyRight) {
                    continue;
                }
            }
            Element imgEl = body.select("img").first();
            if (imgEl != null) {
                articleBody.appendChild(buildFigure(imgEl));
                continue;
            }
            articleBody.appendChild(body);
        }
        return articleBody;
    }
}
