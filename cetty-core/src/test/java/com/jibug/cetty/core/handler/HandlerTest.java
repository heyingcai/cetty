package com.jibug.cetty.core.handler;

import com.jibug.cetty.core.Cetty;
import com.jibug.cetty.core.Seed;

/**
 * @author heyingcai
 */
public class HandlerTest {

    public static void main(String[] args) {
        Cetty cetty = new Cetty();

        HandlerPipeline pipeline = cetty.pipeline();

        pipeline.addLast(new HandlerInitializer() {
            @Override
            public void initCetty(Cetty cetty) {
                HandlerPipeline pipeline = cetty.pipeline();
                pipeline.addLast(new HttpDownloadHandler());
                pipeline.addLast(new PageProcessHandler());
            }
        });

        pipeline.start();

        Seed seed = new Seed("");

        pipeline.download(seed);
    }

}
