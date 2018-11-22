package com.jibug.cetty.core.handler;

import com.jibug.cetty.core.Cetty;
import com.jibug.cetty.core.Page;
import com.jibug.cetty.core.Seed;

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
