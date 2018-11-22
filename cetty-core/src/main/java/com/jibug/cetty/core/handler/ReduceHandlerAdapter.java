package com.jibug.cetty.core.handler;

import com.jibug.cetty.core.Page;

/**
 * @author heyingcai
 */
public class ReduceHandlerAdapter implements ReduceHandler {

    @Override
    public void reduce(HandlerContext ctx, Page page) {
        ctx.fireReduce(page);
    }
}
