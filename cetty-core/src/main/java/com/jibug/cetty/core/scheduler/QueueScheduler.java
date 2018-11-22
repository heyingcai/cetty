package com.jibug.cetty.core.scheduler;

import com.jibug.cetty.core.Seed;
import com.google.common.collect.Queues;

import java.util.concurrent.BlockingQueue;

/**
 * @author heyingcai
 */
public class QueueScheduler implements Scheduler {

    private BlockingQueue<Seed> queue = Queues.newLinkedBlockingQueue();

    @Override
    public void push(Seed seed) {
        queue.add(seed);
    }

    @Override
    public Seed poll() {
        return queue.poll();
    }
}
