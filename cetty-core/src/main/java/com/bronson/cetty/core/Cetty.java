package com.bronson.cetty.core;

import com.bronson.cetty.core.concurrent.NamedThreadFactory;
import com.bronson.cetty.core.handler.HandlerPipeline;
import com.bronson.cetty.core.handler.HttpDownloadHandler;
import com.bronson.cetty.core.net.AsyncHttpClientGenerator;
import com.bronson.cetty.core.net.HttpClientGenerator;
import com.bronson.cetty.core.net.SyncHttpClientGenerator;
import com.bronson.cetty.core.scheduler.QueueScheduler;
import com.bronson.cetty.core.scheduler.Scheduler;
import com.google.common.collect.Lists;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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

    private ThreadPoolExecutor threadPoolExecutor;

    private int threadNum = 1;

    private Thread mainThread = null;

    private ReentrantLock newTask = new ReentrantLock();

    private Condition newTaskCondition = newTask.newCondition();

    private long newTaskWaitTime = 60000;

    private List<Seed> startSeeds;

    /**
     * crawler is support async
     * default value is sync
     */
    private boolean async = false;

    private HttpClientGenerator<CloseableHttpAsyncClient> asyncHttpClientGenerator;

    private HttpClientGenerator<CloseableHttpClient> httpClientHttpClientGenerator;

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
        // downloader handler must have one
        boolean hasDownloadHandler = pipeline.checkDownloadHandler();
        if (!hasDownloadHandler) {
            pipeline.addLast(new HttpDownloadHandler(), "downloader");
        }
    }

    public Cetty setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public Payload getPayload() {
        return payload;
    }

    public Cetty setStartUrl(String url) {
        checkRunningStat();
        this.startSeeds = Arrays.asList(new Seed(url));
        return this;
    }

    public Cetty setStartUrls(List<String> urls) {
        checkRunningStat();
        this.startSeeds = convertSeed(urls);
        return this;
    }

    public Cetty setStartSeed(Seed seed) {
        checkRunningStat();
        this.startSeeds = Arrays.asList(seed);
        return this;
    }

    public Cetty setStartSeeds(List<Seed> seeds) {
        checkRunningStat();
        this.startSeeds = seeds;
        return this;
    }

    private List<Seed> convertSeed(List<String> urls) {
        List<Seed> seeds = Lists.newArrayListWithCapacity(urls.size());
        urls.forEach(url -> {
            seeds.add(new Seed(url));
        });
        return seeds;
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

    public Scheduler getScheduler() {
        return scheduler;
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

    public HttpClientGenerator<CloseableHttpAsyncClient> getAsyncHttpClientGenerator() {
        return asyncHttpClientGenerator;
    }

    public HttpClientGenerator<CloseableHttpClient> getHttpClientHttpClientGenerator() {
        return httpClientHttpClientGenerator;
    }

    public Cetty setThreadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    @Override
    public void run() {
        checkRunningStat();
        initComponent();
        logger.info("Crawler started!");
        while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
            Seed seed = scheduler.poll();

            if (seed == null) {
                if (threadPoolExecutor.getActiveCount() == 0 || stat.get() == STAT_STOPPED) {
                    break;
                }
                waitTask();
            } else {
                threadPoolExecutor.execute(() -> {
                    try {
                        pipeline.download(seed, async);
                    } catch (Exception e) {
                        logger.error("Cetty crawler run error {}", e);
                    } finally {
                        signalTask();
                    }
                });
            }
        }

        stopCrawler();
        mainThread = null;
        stat.set(STAT_INIT);
    }

    public void stopCrawler() {
        if (!stat.compareAndSet(STAT_RUNNING, STAT_STOPPED)) {
            return;
        }

        logger.info("Cetty crawler closed!");

        if (mainThread != null && !mainThread.isInterrupted()) {
            mainThread.interrupt();
        }
    }

    public void startCrawler() {
        if (mainThread != null) {
            return;
        }
        synchronized (this) {
            if (mainThread != null) {
                return;
            }
            Thread mainThread = new Thread(this, "Cetty-crawler");
            mainThread.setDaemon(false);
            this.mainThread = mainThread;
            this.mainThread.start();
        }
    }

    private void waitTask() {
        newTask.lock();

        try {
            if (threadPoolExecutor.getActiveCount() == 0 || stat.get() == STAT_STOPPED) {
                return;
            }
            try {
                newTaskCondition.await(newTaskWaitTime, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.error("waitNewTask interrupted, error {}", e);
            }
        } finally {
            newTask.unlock();
        }
    }

    private void signalTask() {
        try {
            newTask.lock();
            newTaskCondition.signalAll();
        } finally {
            newTask.unlock();
        }
    }

    protected void checkRunningStat() {
        if (stat.get() == STAT_RUNNING) {
            throw new IllegalStateException("Crawler is already running!");
        }
    }

    private void pushSeed(Seed seed) {
        if (seed != null && seed.getUrl() != null) {
            scheduler.push(seed);
        }
    }

    private void initComponent() {
        HandlerPipeline pipeline = this.pipeline();

        mainThread = Thread.currentThread();

        if (async) {
            asyncHttpClientGenerator = new AsyncHttpClientGenerator();
        } else {
            httpClientHttpClientGenerator = new SyncHttpClientGenerator();
        }

        boolean threadPoolAvailable = threadNum > 0 && threadPoolExecutor == null || threadPoolExecutor.isShutdown();
        if (threadPoolAvailable) {
            threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(), new NamedThreadFactory("Cetty-crawler", false));
        }

        if (startSeeds != null) {
            startSeeds.forEach(seed -> {
                pushSeed(seed);
            });
        }

        pipeline.start();

        stat.set(STAT_RUNNING);
    }

}
