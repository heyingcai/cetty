package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Cetty;
import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.Seed;

/**
 * @author heyingcai
 */
public interface HandlerContext {

    /**
     * return page
     *
     * @return
     */
    Cetty cetty();

    /**
     * return pipeline
     *
     * @return
     */
    HandlerPipeline pipeline();

    /**
     * return handler
     *
     * @return
     */
    Handler handler();

    /**
     * trigger receive page request
     */
    void fireReceive();

    /**
     * pass the download event to next handler
     *
     * @param seed
     */
    void fireDownload(Seed seed);

    /**
     * pass the process event to next handler
     *
     * @param page
     */
    void fireProcess(Page page);

    /**
     * pass the reduce event to next handler
     *
     * @param page
     */
    void fireReduce(Page page);


}
