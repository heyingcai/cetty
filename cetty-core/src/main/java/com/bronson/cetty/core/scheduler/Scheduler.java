package com.bronson.cetty.core.scheduler;

import com.bronson.cetty.core.Seed;

/**
 * @author heyingcai
 */
public interface Scheduler {

    void push(Seed seed);

    Seed poll();

}
