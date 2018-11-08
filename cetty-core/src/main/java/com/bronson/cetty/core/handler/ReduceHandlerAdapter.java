package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Result;

/**
 * @author heyingcai
 */
public class ReduceHandlerAdapter implements ReduceHandler {

    @Override
    public void reduce(HandlerContext ctx, Result result) {
        ctx.fireReduce(result);
    }
}
