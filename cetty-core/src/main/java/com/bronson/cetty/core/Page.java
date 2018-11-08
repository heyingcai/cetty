package com.bronson.cetty.core;

import com.bronson.cetty.core.handler.HandlerPipeline;

/**
 * @author heyingcai
 */
public class Page {

    private Payload payload;

    private HandlerPipeline pipeline;

    public Page() {
        this.pipeline = new HandlerPipeline(this);
    }

    public HandlerPipeline pipeline() {
        return pipeline;
    }

}
