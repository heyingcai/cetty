package com.jibug.cetty.core.handler;

import com.jibug.cetty.core.Page;
import com.jibug.cetty.core.Seed;

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
        ctx.fireReduce(page);
    }
}
