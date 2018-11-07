package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;

/**
 * @author heyingcai
 */
public abstract class AbstractHandlerContext implements HandlerContext {

    volatile AbstractHandlerContext prev;
    volatile AbstractHandlerContext next;

    private final boolean processEvent;
    private final boolean reduceEvent;
    private final HandlerPipeline pipeline;
    private final String name;

    public AbstractHandlerContext(boolean processEvent, boolean reduceEvent, HandlerPipeline pipeline, String name) {
        this.processEvent = processEvent;
        this.reduceEvent = reduceEvent;
        this.pipeline = pipeline;
        this.name = name;
    }

    public HandlerPipeline pipeline() {
        return pipeline;
    }

    public Page page() {
        return pipeline.page();
    }

    public String name() {
        return name;
    }

    private AbstractHandlerContext findContextProcess() {
        AbstractHandlerContext ctx = this;
        do {
            ctx = ctx.next;
        } while (!ctx.processEvent);
        return ctx;
    }

    private AbstractHandlerContext findContextReduce() {
        AbstractHandlerContext ctx = this;
        do {
            ctx = ctx.prev;
        } while (!ctx.reduceEvent);
        return ctx;
    }

    public void fireReduce() {
        final AbstractHandlerContext next = findContextReduce();
        next.invokeReduce();
    }


    private void invokeReduce() {
        ReduceHandler reduceHandler = (ReduceHandler) handler();
        reduceHandler.reduce(this);
    }


    public void fireProcess() {
        final AbstractHandlerContext next = findContextProcess();
        next.invokeProcess();

    }

    private void invokeProcess() {
        ProcessHandler processHandler = (ProcessHandler) handler();
        processHandler.process(this);
    }

    public void receive() {
        AbstractHandlerContext head = findContextProcess();
        head.invokeReceive();

    }

    private void invokeReceive() {
        ProcessHandler processHandler = (ProcessHandler) handler();
        processHandler.receive(this);

    }
}
