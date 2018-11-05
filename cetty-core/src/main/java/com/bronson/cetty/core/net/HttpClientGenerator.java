package com.bronson.cetty.core.net;

/**
 * @author heyingcai
 */
public interface HttpClientGenerator<T> {

    /**
     * get the real httpclient instance
     *
     * @param payload
     * @return
     */
    T getClient(Payload payload);
}
