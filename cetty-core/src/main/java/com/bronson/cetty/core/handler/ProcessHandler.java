package com.bronson.cetty.core.handler;

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
    void receive(AbstractHandlerContext ctx);

    /**
     * process the request
     *
     * @param ctx
     */
    void process(AbstractHandlerContext ctx);
}
