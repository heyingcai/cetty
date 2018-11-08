package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Result;
import com.bronson.cetty.core.Payload;

/**
 * @author heyingcai
 */
public abstract class ProcessHandlerAdapter implements ProcessHandler {

    @Override
    public void receive(HandlerContext ctx) {
        ctx.fireReceive();
    }

    @Override
    public void download(HandlerContext ctx, Payload payload) {
        ctx.fireDownload(payload);
    }

    @Override
    public void process(HandlerContext ctx, Result result) {
        ctx.fireProcess(result);
    }

    public abstract Payload getPayload();
}
