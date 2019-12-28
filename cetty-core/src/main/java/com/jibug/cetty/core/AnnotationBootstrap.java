package com.jibug.cetty.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heyingcai
 */
public class AnnotationBootstrap extends Bootstrap {

    private List<Class<?>> executorClasses = new ArrayList<>();

    public AnnotationBootstrap(Class<?> clazz) {
        this.executorClasses.add(clazz);
    }

    public AnnotationBootstrap(List<Class<?>> classes) {
        this.executorClasses.addAll(classes);
    }

    @Override
    public void start() {
        super.start();
    }

    public void execute() {
        if (this.executorClasses.size() == 0) {
            throw new IllegalArgumentException("The Crawler Annotation class not found!");
        }
    }
}
