package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Cetty;
import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Seed;

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
    public void download(HandlerContext ctx, Seed seed) {

    }

    @Override
    public void process(HandlerContext ctx, Page page) {

    }

    /**
     * init page
     *
     * @param cetty
     */
    public abstract void initCetty(Cetty cetty);

    private void initPages(HandlerContext ctx) {
        try {
            Cetty cetty = ctx.cetty();
            initCetty(cetty);
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
