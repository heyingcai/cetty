package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Cetty;
import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Seed;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heyingcai
 */
public class HandlerPipeline {

    private Cetty cetty;
    final AbstractHandlerContext head;
    final AbstractHandlerContext tail;

    final AtomicInteger processCounter = new AtomicInteger(0);
    final AtomicInteger reduceCounter = new AtomicInteger(0);

    public HandlerPipeline(Cetty cetty) {
        this.cetty = cetty;
        head = new HeadContext(this);
        tail = new TailContext(this);

        head.next = tail;
        tail.prev = head;
    }

    public Cetty cetty() {
        return cetty;
    }

    public boolean checkDownloadHandler() {
        AbstractHandlerContext context = head.next;
        while (context != null) {
            if (context.isProcessEvent() && context.name().equals("downloader")) {
                return false;
            }
            context = context.next;
        }
        return false;
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

    public HandlerPipeline addLast(Handler handler, String name) {
        DefaultHandlerContext newCtx = null;
        if (handler instanceof ReduceHandler) {
            newCtx = new DefaultHandlerContext(this, name, handler);
        } else if (handler instanceof ProcessHandler) {
            newCtx = new DefaultHandlerContext(this, name, handler);
        } else {
            throw new IllegalArgumentException("handler must be ProcessHandler or ReduceHandler");
        }
        addLast0(newCtx);
        return this;
    }

    public HandlerPipeline addLast(Handler handler) {
        DefaultHandlerContext newCtx = null;
        if (handler instanceof ReduceHandler) {
            newCtx = new DefaultHandlerContext(this, "ReduceHandler#" + reduceCounter.getAndAdd(1), handler);
        } else if (handler instanceof ProcessHandler) {
            newCtx = new DefaultHandlerContext(this, "ProcessHandler#" + processCounter.getAndAdd(1), handler);
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

    public void download(Seed seed, boolean async) {
        head.fireDownload(seed, async);
    }


    final class HeadContext extends AbstractHandlerContext implements ProcessHandler, ReduceHandler {

        public HeadContext(HandlerPipeline pipeline) {
            super(false, true, pipeline, "head");
        }

        @Override
        public void receive(HandlerContext ctx) {

        }

        @Override
        public void download(HandlerContext ctx, Seed seed, boolean async) {

        }

        @Override
        public void process(HandlerContext ctx, Page page) {

        }

        @Override
        public Handler handler() {
            return this;
        }

        @Override
        public void reduce(HandlerContext ctx, Page page) {
            ctx.fireReduce(page);
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
        public void download(HandlerContext ctx, Seed seed, boolean async) {

        }

        @Override
        public void process(HandlerContext ctx, Page page) {

        }

        @Override
        public Handler handler() {
            return this;
        }

    }

}
