package com.jibug.cetty.core.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author heyingcai
 */
public class CettyThreadPool {

    private static final String name = "CettyThreadPool";

    /**
     * return new Executor
     *
     * @param threads   corePoolSize
     * @param queueSize task queue size
     * @return
     */
    public static Executor newExecutor(int threads, int queueSize) {
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                queueSize == 0 ? new SynchronousQueue<>() : (queueSize < 0 ? new LinkedBlockingQueue<>() : new LinkedBlockingQueue<>(queueSize)),
                new NamedThreadFactory(name, true), new CettyAbortPolicy(name));
    }

}
