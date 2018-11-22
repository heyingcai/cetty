package com.jibug.cetty.core.net;

import com.jibug.cetty.core.Payload;

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
