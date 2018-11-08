package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Result;

/**
 * @author heyingcai
 */
public class PipelineHandler extends ReduceHandlerAdapter{

    @Override
    public void reduce(HandlerContext ctx, Result result) {
        System.out.println(result.toString());
    }
}
