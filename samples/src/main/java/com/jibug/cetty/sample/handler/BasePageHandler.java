package com.jibug.cetty.sample.handler;

import com.google.common.base.Strings;
import com.jibug.cetty.core.Page;
import com.jibug.cetty.core.handler.HandlerContext;
import com.jibug.cetty.core.handler.ProcessHandlerAdapter;
import com.jibug.cetty.sample.entity.Article;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author heyingcai
 */
public abstract class BasePageHandler extends ProcessHandlerAdapter {

    /**
     * 路由解析
     *
     * @param ctx
     * @param page
     */
    protected abstract void parseRoute(HandlerContext ctx, Page page);

    /**
     * 解析文章列表抽象方法
     *
     * @param ctx
     * @param page
     */
    protected abstract void parseListing(HandlerContext ctx, Page page);

    /**
     * 解析文章内容体抽象方法
     *
     * @param ctx
     * @param page
     */
    protected abstract void parseBody(HandlerContext ctx, Page page);

    /**
     * 生成我们自己的文章内容体
     *
     * @param tempBody
     */
    public Element appendBody(Elements tempBody) {
        final Element articleBody = new Element(Tag.valueOf("div"), "");
        for (final Element pEl : tempBody) {
            Element imgEl = pEl.select("img").first();
            if (imgEl != null) {
                articleBody.appendChild(buildFigure(imgEl));
                continue;
            }
            articleBody.appendChild(pEl);
        }
        return articleBody;
    }

    /**
     * 解析图片格式
     *
     * @param bodyElement
     * @return
     */
    protected Element buildFigure(Element bodyElement) {
        final Element figure = new Element(Tag.valueOf("figure"), "");
        figure.appendChild(bodyElement);
        return figure;
    }

    /**
     * 解析图文格式
     *
     * @param figcaptionText
     * @return
     */
    protected Element buildFigcaption(String figcaptionText) {
        final Element figcaption = new Element(Tag.valueOf("figcaption"), "");
        figcaption.append(figcaptionText);
        return figcaption;
    }

    /**
     * 解析段落块格式
     *
     * @param blockquote
     * @param articleBody
     */
    protected void buildBlockquote(String blockquote, Element articleBody) {
        final Element blockquoteEl = new Element(Tag.valueOf("blockquote"), "");
        blockquoteEl.append(blockquote);
        articleBody.appendChild(blockquoteEl);
    }

    public void buildArticle(Article article, Page page, Element articleBody) {
        article.setContent(articleBody.toString());
        article.setVia(page.getSeed().getAttach("via").toString());
        article.setUrl(page.getUrl());
        article.setSummary(page.getSeed().getAttach("summary").toString());
        Object listPhoto = page.getSeed().getAttach("listPhoto");
        article.setListPhoto(listPhoto == null ? null : listPhoto.toString());
        article.setTitle(page.getSeed().getAttach("title").toString());
    }

    public static String dealDateFormat(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        Date date = sd.parse(dateStr);
        return sdf.format(date);
    }

    public String regex(String str, String regex) {
        final Pattern p = Pattern.compile(regex);
        final Matcher match = p.matcher(str);
        if (!match.find()) {
            return null;
        }

        String Str = match.group(1);
        if (Strings.isNullOrEmpty(Str)) {
            return null;
        }
        return Str;
    }

}
