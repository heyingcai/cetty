package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;

/**
 * page handler initial
 *
 * @author heyingcai
 */
public abstract class HandlerInitializer implements ProcessHandler {

    @Override
    public void receive(AbstractHandlerContext ctx) {
        initPages(ctx);
        ctx.receive();
    }

    /**
     * init page
     *
     * @param page
     */
    public abstract void initPage(Page page);

    private void initPages(AbstractHandlerContext ctx) {
        try {
            Page channel = ctx.page();
            initPage(channel);
        } finally {
            remove(ctx);
        }
    }

    private void remove(AbstractHandlerContext ctx) {
        HandlerPipeline pipeline = ctx.pipeline();
        if (pipeline.context(this) != null) {
            pipeline.remove(this);
        }
    }

}
