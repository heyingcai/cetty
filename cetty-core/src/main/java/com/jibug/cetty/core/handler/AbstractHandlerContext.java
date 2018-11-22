package com.jibug.cetty.core.handler;

import com.jibug.cetty.core.Cetty;
import com.jibug.cetty.core.Page;
import com.jibug.cetty.core.Seed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author heyingcai
 */
public abstract class AbstractHandlerContext implements HandlerContext {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHandlerContext.class);

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
    public void fireReduce(Page page) {
        final AbstractHandlerContext next = findContextReduce();
        if (next.prev != null && next.prev.isProcessEvent()) {
            if (!page.getNextSeeds().isEmpty()) {
                page.getNextSeeds().forEach(seed -> pipeline.cetty().getScheduler().push(seed));
            }
        }
        next.invokeReduce(page);
        if (next.next.name().equals("tail")) {
            try {
                Thread.sleep(pipeline.cetty().getPayload().getSleepTime());
            } catch (InterruptedException e) {
                logger.error("Thread interrupted when sleep", e);
            }
        }
    }

    private void invokeReduce(Page page) {
        ReduceHandler reduceHandler = (ReduceHandler) handler();
        reduceHandler.reduce(this, page);
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

    public boolean isProcessEvent() {
        return processEvent;
    }

    public boolean isReduceEvent() {
        return reduceEvent;
    }
}
