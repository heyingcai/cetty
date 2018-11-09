package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Result;

/**
 * @author heyingcai
 */
public class PageProcessHandler extends ProcessHandlerAdapter {

    @Override
    public void process(HandlerContext ctx, Page page) {
        Result result = page.getResult();

        ctx.fireReduce(result);
    }

}
