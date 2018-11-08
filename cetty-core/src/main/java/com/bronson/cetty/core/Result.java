package com.bronson.cetty.core;

/**
 * @author heyingcai
 */
public class Result {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Result{" +
                "name='" + name + '\'' +
                '}';
    }
}
