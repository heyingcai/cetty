package com.bronson.cetty.core.net;

import com.bronson.cetty.core.Payload;

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
