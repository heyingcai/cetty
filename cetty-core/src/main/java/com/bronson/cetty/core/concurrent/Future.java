package com.bronson.cetty.core.concurrent;

/**
 * This is the result of an asynchronous operation.
 *
 * @author heyingcai
 * @param <V>
 */
@SuppressWarnings("ClassNameSameAsAncestorName")
public interface Future<V> extends java.util.concurrent.Future<V> {

    boolean isSuccess();

    Future<V> addListener(GenericFutureListener<? extends Future<? super V>> listener);

    Future<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners);

    Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener);

    Future<V> removeListemers(GenericFutureListener<? extends Future<? super V>>... listeners);

    Future<V> await() throws InterruptedException;

    V getNow();
}
