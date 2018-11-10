package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Seed;
import com.bronson.cetty.core.scheduler.Scheduler;

/**
 * @author heyingcai
 */
public abstract class ProcessHandlerAdapter implements ProcessHandler {

    @Override
    public void receive(HandlerContext ctx) {
        ctx.fireReceive();
    }

    @Override
    public void download(HandlerContext ctx, Seed seed, Scheduler scheduler, boolean async) {
        ctx.fireDownload(seed,scheduler,async);
    }

    @Override
    public void process(HandlerContext ctx, Page page) {
        ctx.fireReduce(page.getResult());
    }
}
