package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.Result;

/**
 * page handler initial
 *
 * @author heyingcai
 */
public abstract class HandlerInitializer implements ProcessHandler {

    @Override
    public void receive(HandlerContext ctx) {
        initPages(ctx);
        ctx.fireReceive();
    }

    @Override
    public void download(HandlerContext ctx, Payload payload) {

    }

    @Override
    public void process(HandlerContext ctx, Result result) {

    }

    /**
     * init page
     *
     * @param page
     */
    public abstract void initPage(Page page);

    private void initPages(HandlerContext ctx) {
        try {
            Page channel = ctx.page();
            initPage(channel);
        } finally {
            remove(ctx);
        }
    }

    private void remove(HandlerContext ctx) {
        HandlerPipeline pipeline = ctx.pipeline();
        if (pipeline.context(this) != null) {
            pipeline.remove(this);
        }
    }

}
