package com.jibug.cetty.core.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * refer to webmagic
 * https://github.com/code4craft/webmagic/blob/master/webmagic-core/src/main/java/us/codecraft/webmagic/thread/CountableThreadPool.java
 *
 * @author heyingcai
 */
public class CountableThreadPool {

    private int threadNum;

    private AtomicInteger threadAlive = new AtomicInteger();

    private ReentrantLock reentrantLock = new ReentrantLock();

    private Condition condition = reentrantLock.newCondition();

    private ThreadPoolExecutor threadPoolExecutor;

    public CountableThreadPool(int threadNum) {
        this.threadNum = threadNum;
        this.threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NamedThreadFactory("Cetty-crawler", false));
    }

    public CountableThreadPool(int threadNum, ThreadPoolExecutor threadPoolExecutor) {
        this.threadNum = threadNum;
        this.threadPoolExecutor = threadPoolExecutor;
    }


    public int getThreadAliveCount() {
        return threadAlive.get();
    }

    public int getThreadNum() {
        return threadNum;
    }

    public boolean isShutdown() {
        return threadPoolExecutor.isShutdown();
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void execute(final Runnable runnable) {


        if (threadAlive.get() >= threadNum) {
            try {
                reentrantLock.lock();
                while (threadAlive.get() >= threadNum) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        threadAlive.incrementAndGet();
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {
                    try {
                        reentrantLock.lock();
                        threadAlive.decrementAndGet();
                        condition.signal();
                    } finally {
                        reentrantLock.unlock();
                    }
                }
            }
        });
    }

}
