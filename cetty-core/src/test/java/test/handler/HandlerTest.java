package test.handler;

import com.bronson.cetty.core.Page;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.handler.DownloadHandler;
import com.bronson.cetty.core.handler.HandlerInitializer;
import com.bronson.cetty.core.handler.HandlerPipeline;
import com.bronson.cetty.core.handler.PageProcessHandler;
import com.bronson.cetty.core.handler.PipelineHandler;

/**
 * @author heyingcai
 */
public class HandlerTest {

    public static void main(String[] args) {
        Page page = new Page();
        HandlerPipeline pipeline = page.pipeline();

        pipeline.addLast(new HandlerInitializer() {
            @Override
            public void initPage(Page page) {
                HandlerPipeline pipeline = page.pipeline();
                pipeline.addLast(new DownloadHandler());
                pipeline.addLast(new PageProcessHandler());
                pipeline.addLast(new PipelineHandler());
            }
        });

        pipeline.start();

        Payload payload = new Payload();
        payload.setDomain("http:www.baidu.com");
        pipeline.download(payload);
    }

}
