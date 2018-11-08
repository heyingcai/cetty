package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Result;
import com.bronson.cetty.core.Payload;

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

    @Override
    public HandlerPipeline pipeline() {
        return pipeline;
    }

    @Override
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
            ctx = ctx.next;
        } while (!ctx.reduceEvent);
        return ctx;
    }

    @Override
    public void fireDownload(Payload payload) {
        final AbstractHandlerContext next = findContextProcess();
        next.invokeDownload(payload);
    }

    private void invokeDownload(Payload payload) {
        ProcessHandler processHandler = (ProcessHandler) handler();
        processHandler.download(this, payload);
    }

    @Override
    public void fireReduce(Result result) {
        final AbstractHandlerContext next = findContextReduce();
        next.invokeReduce(result);
    }

    private void invokeReduce(Result result) {
        ReduceHandler reduceHandler = (ReduceHandler) handler();
        reduceHandler.reduce(this, result);
    }

    @Override
    public void fireProcess(Result result) {
        final AbstractHandlerContext next = findContextProcess();
        next.invokeProcess(result);

    }

    private void invokeProcess(Result result) {
        ProcessHandler processHandler = (ProcessHandler) handler();
        processHandler.process(this, result);
    }

    @Override
    public void fireReceive() {
        AbstractHandlerContext head = findContextProcess();
        head.invokeReceive();

    }

    private void invokeReceive() {
        ProcessHandler processHandler = (ProcessHandler) handler();
        processHandler.receive(this);
    }
}
