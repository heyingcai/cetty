package com.bronson.cetty.core;

import com.bronson.cetty.core.handler.HandlerPipeline;
import com.bronson.cetty.core.scheduler.QueueScheduler;
import com.bronson.cetty.core.scheduler.Scheduler;

/**
 * @author heyingcai
 * @date 2018/7/3
 */
public class Cetty implements Runnable {

    /**
     * crawler is support async
     * default value is sync
     */
    private boolean async = false;

    /**
     * the crawler global handler
     * these handler all in the pipeline
     */
    private HandlerPipeline pipeline;

    private Scheduler scheduler = new QueueScheduler();

    public Cetty() {
        this.pipeline = new HandlerPipeline(this);
    }

    public HandlerPipeline pipeline() {
        return pipeline;
    }


    @Override
    public void run() {

    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
