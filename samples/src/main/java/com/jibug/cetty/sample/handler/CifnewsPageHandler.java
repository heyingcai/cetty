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
 * 雨果网抓取
 * https://www.cifnews.com/Search/1?keyword=shopify
 *
 * @author heyingcai
 */
@Component
public class CifnewsPageHandler extends BasePageHandler {

    private static final Pattern PAGE_REGEX_PATTERN = Pattern.compile("/Search/(\\d+)");


    @Override
    public void process(HandlerContext ctx, Page page) {
        parseRoute(ctx, page);
    }

    @Override
    protected void parseRoute(HandlerContext ctx, Page page) {
        String pageUrl = page.getUrl();
        if (pageUrl.contains("/Search/")) {
            parseListing(ctx, page);
        } else {
            parseBody(ctx, page);
        }
    }

    @Override
    protected void parseListing(HandlerContext ctx, Page page) {
        Document document = page.getDocument();

        Elements articles = document.select("ul.search_list>li");

        List<Seed> seeds = new ArrayList<>();
        for (Element article : articles) {
            String url = article.select("a").attr("abs:href");
            String title = article.select("a").text();

            String summary = article.select("p").text();

            Seed seed = new Seed(url);
            seed.putAttach("via", page.getSeed().getAttach("via"));
            seed.putAttach("summary", summary);
            seed.putAttach("title", title);

            seeds.add(seed);
        }

        Matcher matcher = PAGE_REGEX_PATTERN.matcher(page.getUrl());
        if (!matcher.find()) {
            return;
        }

        final String pageNumStr = matcher.group(0).replace("/Search/", "");
        int nextPageNum = Integer.parseInt(pageNumStr);
        int pageLimit = Integer.parseInt(page.getSeed().getAttach("pageLimit").toString());

        if (++nextPageNum <= pageLimit) {
            String nextPageUrl = String.format("https://www.cifnews.com/Search/%d?keyword=shopify", nextPageNum);
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

        Elements content = document.select("div.article-box>div.leftcont>*");

        String publishTime = content.select("div.info-bar>div.time").text();
        Article article = new Article();
        article.setPublishTime(publishTime);

        buildArticle(article, page, appendBody(content));

        page.getResult().putField("article", article);

        ctx.fireReduce(page);
    }

    @Override
    public Element appendBody(Elements tempBody) {
        final Element articleBody = new Element(Tag.valueOf("div"), "");
        String blockquote = tempBody.select("div.fetch-read>div.summary").text();
        buildBlockquote(blockquote, articleBody);
        Elements inner = tempBody.select("div.article-inner>*");
        for (Element pEl : inner) {
            if (pEl.select("div.fetch-present").size() != 0) {
                continue;
            }
            Element imgEl = pEl.select("p>img").first();
            if (imgEl != null) {
                Element figure = buildFigure(imgEl);
                if (imgEl.nextElementSibling() != null && imgEl.nextElementSibling().tagName().equals("p")) {
                    Element figcaption = buildFigcaption(imgEl.nextElementSibling().text());
                    figure.appendChild(figcaption);
                    articleBody.appendChild(figure);
                    continue;
                }
                articleBody.appendChild(figure);
                continue;
            }
            articleBody.appendChild(pEl);
        }
        return articleBody;
    }
}
