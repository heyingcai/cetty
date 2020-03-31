package com.jibug.cetty.sample.reduce;

import com.jibug.cetty.core.Page;
import com.jibug.cetty.core.Result;
import com.jibug.cetty.core.handler.HandlerContext;
import com.jibug.cetty.core.handler.ReduceHandlerAdapter;
import com.jibug.cetty.sample.entity.Article;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * reduce聚合处理器
 *
 * @author heyingcai
 */
@Component
public class ArticleReducer extends ReduceHandlerAdapter {


    @Override
    public void reduce(HandlerContext ctx, Page page) {
        Result result = page.getResult();

        Map<String, Object> fieldResult = result.getFieldResult();
        if (fieldResult.isEmpty()) {
            return;
        }

        Article article = (Article) fieldResult.get("article");


        System.out.println(article);

    }

}
