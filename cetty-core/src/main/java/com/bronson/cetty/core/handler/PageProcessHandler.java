package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.Result;

/**
 * @author heyingcai
 */
public class PageProcessHandler extends ProcessHandlerAdapter{

    @Override
    public void process(HandlerContext ctx, Result result) {
        String name = result.getName();
        result.setName("page:" + name);
        ctx.fireReduce(result);
    }

    @Override
    public Payload getPayload() {
        return null;
    }


}
