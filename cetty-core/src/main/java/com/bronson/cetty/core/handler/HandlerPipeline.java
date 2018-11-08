package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.Result;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heyingcai
 */
public class HandlerPipeline {

    private Page page;
    final AbstractHandlerContext head;
    final AbstractHandlerContext tail;

    final AtomicInteger processCounter = new AtomicInteger(0);
    final AtomicInteger reduceCounter = new AtomicInteger(0);

    public HandlerPipeline(Page page) {
        this.page = page;
        head = new HeadContext(this);
        tail = new TailContext(this);

        head.next = tail;
        tail.prev = head;
    }

    public Page page() {
        return page;
    }

    public final AbstractHandlerContext context(Handler handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }

        AbstractHandlerContext ctx = head.next;
        for (; ; ) {
            if (ctx == null) {
                return null;
            }
            if (ctx.handler() == handler) {
                return ctx;
            }
            ctx = ctx.next;
        }
    }

    public final HandlerPipeline remove(Handler handler) {
        remove(getContextOrDie(handler));
        return this;
    }

    private AbstractHandlerContext getContextOrDie(Handler handler) {
        AbstractHandlerContext ctx = context(handler);
        if (ctx == null) {
            throw new NoSuchElementException(handler.getClass().getName());
        } else {
            return ctx;
        }
    }

    private AbstractHandlerContext remove(final AbstractHandlerContext ctx) {
        assert ctx != head && ctx != tail;

        synchronized (this) {
            remove0(ctx);
        }
        return ctx;
    }

    private static void remove0(AbstractHandlerContext ctx) {
        AbstractHandlerContext prev = ctx.prev;
        AbstractHandlerContext next = ctx.next;
        prev.next = next;
        next.prev = prev;
    }

    public HandlerPipeline addLast(Handler handler) {
        DefaultHandlerContext newCtx = null;
        if (handler instanceof ReduceHandler) {
            newCtx = new DefaultHandlerContext(this, "ProcessHandler#" + processCounter.getAndAdd(1), handler);
        } else if (handler instanceof ProcessHandler) {
            newCtx = new DefaultHandlerContext(this, "ReduceHandler#" + reduceCounter.getAndAdd(1), handler);
        } else {
            throw new IllegalArgumentException("handler must be ProcessHandler or ReduceHandler");
        }
        addLast0(newCtx);
        return this;
    }

    private void addLast0(AbstractHandlerContext newCtx) {
        AbstractHandlerContext prev = tail.prev;
        newCtx.prev = prev;
        newCtx.next = tail;
        prev.next = newCtx;
        tail.prev = newCtx;
    }


    public void start() {
        head.fireReceive();
    }

    public void download(Payload payload) {
        head.fireDownload(payload);
    }


    final class HeadContext extends AbstractHandlerContext implements ProcessHandler,ReduceHandler {

        public HeadContext(HandlerPipeline pipeline) {
            super(false, true, pipeline, "head");
        }

        @Override
        public void receive(HandlerContext ctx) {

        }

        @Override
        public void download(HandlerContext ctx,Payload payload) {

        }

        @Override
        public void process(HandlerContext ctx, Result result) {

        }

        @Override
        public Handler handler() {
            return this;
        }

        @Override
        public void reduce(HandlerContext ctx, Result result) {
            ctx.fireReduce(result);
        }
    }

    final class TailContext extends AbstractHandlerContext implements ProcessHandler {

        public TailContext(HandlerPipeline pipeline) {
            super(true, false, pipeline, "tail");
        }

        @Override
        public void receive(HandlerContext ctx) {

        }

        @Override
        public void download(HandlerContext ctx, Payload payload) {

        }

        @Override
        public void process(HandlerContext ctx, Result result) {

        }

        @Override
        public Handler handler() {
            return this;
        }
    }

}
