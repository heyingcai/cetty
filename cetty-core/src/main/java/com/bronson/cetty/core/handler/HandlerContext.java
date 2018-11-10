package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Cetty;
import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Result;
import com.bronson.cetty.core.Seed;
import com.bronson.cetty.core.scheduler.Scheduler;

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
     * @param scheduler
     * @param async
     */
    void fireDownload(Seed seed, Scheduler scheduler, boolean async);

    /**
     * pass the process event to next handler
     *
     * @param page
     */
    void fireProcess(Page page);

    /**
     * pass the reduce event to next handler
     *
     * @param result
     */
    void fireReduce(Result result);


}
