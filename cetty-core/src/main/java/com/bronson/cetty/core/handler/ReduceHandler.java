package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Result;

/**
 * @author heyingcai
 */
public interface ReduceHandler extends Handler {

    /**
     * reduce the request result from process handler
     *
     * @param ctx
     * @param result
     */
    void reduce(HandlerContext ctx, Result result);

}
