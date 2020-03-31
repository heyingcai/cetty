package com.jibug.cetty.core.context;

import com.google.common.collect.Maps;
import com.jibug.cetty.core.Bootstrap;

import java.util.Map;

/**
 * @author heyingcai
 */
public final class CettyContext {

    private static Map<String, Bootstrap> contextMap = Maps.newConcurrentMap();

    public static Map<String, Bootstrap> getContextMap() {
        return contextMap;
    }

    public static void addContext(String key, Bootstrap bootstrap) {
        if (contextMap.get(key) == null) {
            contextMap.put(key, bootstrap);
        }
    }

    public static Bootstrap getContext(String key) {
        return contextMap.get(key);
    }
}
