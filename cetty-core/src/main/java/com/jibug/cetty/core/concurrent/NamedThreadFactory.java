package com.jibug.cetty.core.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heyingcai
 */
public class NamedThreadFactory implements ThreadFactory {

    /**
     * define current thread factory number
     */
    private static final AtomicInteger poolId = new AtomicInteger(1);

    /**
     * define current thread number
     */
    private final AtomicInteger mThreadNumber = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemon;

    protected final ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("cetty-threadpool-" + poolId.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        this.prefix = prefix + "-thread-";
        this.daemon = daemon;
        SecurityManager securityManager = System.getSecurityManager();
        threadGroup = (securityManager == null) ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    public NamedThreadFactory(String prefix, boolean daemon, ThreadGroup threadGroup) {
        this.prefix = prefix;
        this.daemon = daemon;
        this.threadGroup = threadGroup;
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = prefix + mThreadNumber.getAndIncrement();
        Thread thread = new Thread(threadGroup, runnable, name, 0);
        thread.setDaemon(daemon);
        return thread;
    }
}
