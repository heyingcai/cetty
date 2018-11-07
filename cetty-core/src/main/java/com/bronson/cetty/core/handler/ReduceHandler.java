package com.bronson.cetty.core.handler;

/**
 * @author heyingcai
 */
public interface ReduceHandler extends Handler {

    /**
     * reduce the request from process handler
     *
     * @param ctx
     */
    void reduce(AbstractHandlerContext ctx);

}
