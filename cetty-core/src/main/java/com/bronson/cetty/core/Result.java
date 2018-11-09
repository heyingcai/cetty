package com.bronson.cetty.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author heyingcai
 */
public class Result {

    private Seed seed;

    private List<Object> resultList = Lists.newLinkedList();

    private Map<String, Object> fieldResult = Maps.newHashMap();

    public void addResult(String result) {
        resultList.add(result);
    }

    public void addResult(Object result) {
        resultList.add(result);
    }

    public void addResults(Collection<?> results) {
        resultList.addAll(results);
    }

    public void putField(String key, Object value) {
        fieldResult.put(key, value);
    }

    public void putFieldMap(Map<String, Object> resultMap) {
        fieldResult.putAll(resultMap);
    }

    public List<Object> getResultList() {
        return resultList;
    }

    public Map<String, Object> getFieldResult() {
        return fieldResult;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    public Seed getSeed() {
        return seed;
    }
}
