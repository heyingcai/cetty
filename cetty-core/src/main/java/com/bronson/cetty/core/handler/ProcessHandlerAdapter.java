package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.Seed;

/**
 * @author heyingcai
 */
public abstract class ProcessHandlerAdapter implements ProcessHandler {

    @Override
    public void receive(HandlerContext ctx) {
        ctx.fireReceive();
    }

    @Override
    public void download(HandlerContext ctx, Seed seed) {
        ctx.fireDownload(seed);
    }

    @Override
    public void process(HandlerContext ctx, Page page) {
        ctx.fireReduce(page.getResult());
    }
}
