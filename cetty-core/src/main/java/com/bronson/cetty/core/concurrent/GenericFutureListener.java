package com.bronson.cetty.core.concurrent;

import java.util.EventListener;

/**
 * @author heyingcai
 */
public interface GenericFutureListener<F extends Future<?>> extends EventListener {

    void operationComplete(F future) throws Exception;
}
