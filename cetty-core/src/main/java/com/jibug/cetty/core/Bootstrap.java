package com.jibug.cetty.core;

import com.jibug.cetty.core.handler.Handler;
import com.jibug.cetty.core.handler.ProcessHandlerAdapter;
import com.jibug.cetty.core.scheduler.Scheduler;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * @author heyingcai
 */
public class Bootstrap {

    private Cetty cetty;

    public Bootstrap() {
        cetty = new Cetty();
    }

    public static Bootstrap me() {
        return new Bootstrap();
    }

    public Bootstrap addHandler(Handler handler) {
        cetty.pipeline().addLast(Preconditions.checkNotNull(handler,"handler can not be null"));
        return this;
    }

    public Bootstrap addHandler(Handler handler,String name) {
        cetty.pipeline().addLast(Preconditions.checkNotNull(handler,"handler can not be null"),name);
        return this;
    }

    public Bootstrap setThreadNum(int threadNum) {
        cetty.setThreadNum(threadNum);
        return this;
    }

    public Bootstrap isAsync(boolean async) {
        cetty.setAsync(async);
        return this;
    }

    public Bootstrap startUrl(String url) {
        cetty.setStartUrl(url);
        return this;
    }

    public Bootstrap startUrls(List<String> urls) {
        cetty.setStartUrls(urls);
        return this;
    }

    public Bootstrap startSeed(Seed seed) {
        cetty.setStartSeed(seed);
        return this;
    }

    public Bootstrap startSeeds(List<Seed> seeds) {
        cetty.setStartSeeds(seeds);
        return this;
    }

    public Bootstrap setScheduler(Scheduler scheduler) {
        cetty.setScheduler(scheduler);
        return this;
    }

    public Bootstrap setDownloader(ProcessHandlerAdapter handlerAdapter) {
        cetty.pipeline().addLast(Preconditions.checkNotNull(handlerAdapter,"handler can not be null"));
        return this;
    }

    public Bootstrap setPayload(Payload payload) {
        cetty.setPayload(payload);
        return this;
    }

    public void start() {
        cetty.startCrawler();
    }

    public void stop() {
        cetty.stopCrawler();
    }
}
