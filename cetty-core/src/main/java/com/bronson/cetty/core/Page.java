package com.bronson.cetty.core;

/**
 * @author heyingcai
 */
public class Page {

    private Seed seed;

    private Result result;

    private String rawData;

    private byte[] bytes;

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    public Result getResult() {
        result.setSeed(seed);
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
