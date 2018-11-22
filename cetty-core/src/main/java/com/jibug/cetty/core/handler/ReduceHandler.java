package com.jibug.cetty.core.handler;

import com.jibug.cetty.core.Page;

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
