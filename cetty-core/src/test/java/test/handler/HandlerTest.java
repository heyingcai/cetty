package test.handler;

import com.bronson.cetty.core.Cetty;
import com.bronson.cetty.core.Seed;
import com.bronson.cetty.core.handler.HandlerInitializer;
import com.bronson.cetty.core.handler.HandlerPipeline;
import com.bronson.cetty.core.handler.HttpDownloadHandler;
import com.bronson.cetty.core.handler.PageProcessHandler;
import com.bronson.cetty.core.scheduler.QueueScheduler;
import com.bronson.cetty.core.scheduler.Scheduler;

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
        Scheduler scheduler = new QueueScheduler();

        pipeline.download(seed, scheduler, false);
    }

}
