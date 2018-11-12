package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.Seed;

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
     * @param async
     */
    void download(HandlerContext ctx, Seed seed, boolean async);

    /**
     * process the request
     *
     * @param ctx
     * @param page
     */
    void process(HandlerContext ctx, Page page);
}
