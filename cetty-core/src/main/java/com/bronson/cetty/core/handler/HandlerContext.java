package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Cetty;
import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Result;
import com.bronson.cetty.core.Payload;

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
     * @param payload
     */
    void fireDownload(Payload payload);

    /**
     * pass the process event to next handler
     *
     * @param result
     */
    void fireProcess(Result result);

    /**
     * pass the reduce event to next handler
     *
     * @param result
     */
    void fireReduce(Result result);


}
