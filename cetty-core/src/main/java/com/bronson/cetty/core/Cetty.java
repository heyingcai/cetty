package com.bronson.cetty.core;

import com.bronson.cetty.core.handler.HandlerPipeline;
import com.bronson.cetty.core.handler.HttpDownloadHandler;
import com.bronson.cetty.core.scheduler.QueueScheduler;
import com.bronson.cetty.core.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heyingcai
 * @date 2018/7/3
 */
public class Cetty implements Runnable {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicInteger stat = new AtomicInteger(STAT_INIT);

    private final static int STAT_INIT = 0;

    private final static int STAT_RUNNING = 1;

    private final static int STAT_STOPPED = 2;

    private final static int STAT_STARING = 3;

    private ThreadPoolExecutor threadPoolExecutor;

    private int threadNum = 1;

    /**
     * crawler is support async
     * default value is sync
     */
    private boolean async = false;

    /**
     * crawler request payload
     */
    private Payload payload;

    /**
     * the crawler global handler
     * these handler all in the pipeline
     */
    private HandlerPipeline pipeline;

    /**
     * url scheduler
     */
    private Scheduler scheduler = new QueueScheduler();

    public Cetty() {
        this.pipeline = new HandlerPipeline(this);
    }

    public Cetty setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public Cetty setScheduler(Scheduler scheduler) {
        checkRunningStat();
        Scheduler oldScheduler = this.scheduler;
        this.scheduler = scheduler;
        if (oldScheduler != null) {
            Seed seed;
            while ((seed = oldScheduler.poll()) != null) {
                scheduler.push(seed);
            }
        }
        return this;
    }

    public HandlerPipeline pipeline() {
        return pipeline;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public void run() {
        checkRunningStat();
        initComponent();
        logger.info("Crawler started!");
        while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
            Seed seed = scheduler.poll();

            if (seed == null) {

            }
        }

    }

    protected void checkRunningStat() {
        if (stat.get() == STAT_RUNNING) {
            throw new IllegalStateException("Crawler is already running!");
        }
    }

    private void initComponent() {
        HandlerPipeline pipeline = this.pipeline();

        // handler must have one
        boolean hasDownloadHandler = pipeline.checkDownloadHandler();
        if (!hasDownloadHandler) {
            pipeline.addLast(new HttpDownloadHandler());
        }


    }

}
