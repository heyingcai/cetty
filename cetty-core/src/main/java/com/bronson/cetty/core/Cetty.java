package com.bronson.cetty.core;

import com.bronson.cetty.core.handler.HandlerPipeline;

/**
 * @author heyingcai
 * @date 2018/7/3
 */
public class Cetty implements Runnable {

    /**
     * crawler is support async
     */
    private boolean async;

    /**
     * the crawler global handler
     * these handler all in the pipeline
     */
    private HandlerPipeline pipeline;

    public Cetty() {
        this.pipeline = new HandlerPipeline(this);
    }

    public HandlerPipeline pipeline() {
        return pipeline;
    }


    @Override
    public void run() {

    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
