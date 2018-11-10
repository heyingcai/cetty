package com.bronson.cetty.core.scheduler;

import com.bronson.cetty.core.Seed;

/**
 * @author heyingcai
 */
public interface Scheduler {

    /**
     * push the seed to scheduler
     *
     * @param seed
     */
    void push(Seed seed);

    /**
     * poll the seed from scheduler
     *
     * @return
     */
    Seed poll();

}
