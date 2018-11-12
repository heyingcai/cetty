package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;

/**
 * @author heyingcai
 */
public interface ReduceHandler extends Handler {

    /**
     * reduce the request result from process handler
     *
     * @param ctx
     * @param page
     */
    void reduce(HandlerContext ctx, Page page);

}
