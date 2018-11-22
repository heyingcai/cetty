package com.jibug.cetty.core.handler;

/**
 * @author heyingcai
 */
public class DefaultHandlerContext extends AbstractHandlerContext{

    private Handler handler;

    public DefaultHandlerContext(HandlerPipeline pipeline, String name,Handler handler) {
        super(isProcess(handler), isReduce(handler), pipeline, name);
        this.handler = handler;
    }

    @Override
    public Handler handler() {
        return handler;
    }

    private static boolean isProcess(Handler handler) {
        return handler instanceof ProcessHandler;
    }

    private static boolean isReduce(Handler handler) {
        return handler instanceof ReduceHandler;
    }
}
