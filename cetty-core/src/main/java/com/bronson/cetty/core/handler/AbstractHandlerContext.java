package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Cetty;
import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Result;
import com.bronson.cetty.core.Seed;

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
    public Cetty cetty() {
        return pipeline.cetty();
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
    public void fireDownload(Seed seed) {
        final AbstractHandlerContext next = findContextProcess();
        next.invokeDownload(seed);
    }

    private void invokeDownload(Seed seed) {
        ProcessHandler processHandler = (ProcessHandler) handler();
        processHandler.download(this, seed);
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
    public void fireProcess(Page page) {
        final AbstractHandlerContext next = findContextProcess();
        next.invokeProcess(page);

    }

    private void invokeProcess(Page page) {
        ProcessHandler processHandler = (ProcessHandler) handler();
        processHandler.process(this, page);
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
