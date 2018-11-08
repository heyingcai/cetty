package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Result;
import com.bronson.cetty.core.Payload;

/**
 * process handler
 *
 * @author heyingcai
 */
public interface ProcessHandler extends Handler {

    /**
     * receive new request even
     *
     * @param ctx
     */
    void receive(HandlerContext ctx);

    /**
     * download the request
     *
     * @param ctx
     * @param payload
     */
    void download(HandlerContext ctx, Payload payload);

    /**
     * process the request
     *
     * @param ctx
     * @param result
     */
    void process(HandlerContext ctx, Result result);
}
