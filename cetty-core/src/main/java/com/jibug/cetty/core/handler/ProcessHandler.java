package com.jibug.cetty.core.handler;

import com.jibug.cetty.core.Page;
import com.jibug.cetty.core.Seed;

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
     * @param seed
     */
    void download(HandlerContext ctx, Seed seed);

    /**
     * process the request
     *
     * @param ctx
     * @param page
     */
    void process(HandlerContext ctx, Page page);
}
