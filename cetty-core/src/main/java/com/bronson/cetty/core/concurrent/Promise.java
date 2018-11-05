package com.bronson.cetty.core.concurrent;

/**
 * @param <V>
 * @author heyingcai
 */
public interface Promise<V> extends Future<V> {

    Promise<V> setSuccess(V result);

    boolean trySuccess(V result);

    Promise<V> setFailure(Throwable cause);

    boolean tryFailure(Throwable cause);
}
