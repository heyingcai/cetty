package com.bronson.cetty.core.handler;

import com.bronson.cetty.core.Page;

/**
 * @author heyingcai
 */
public class ConsoleReduceHandler extends ReduceHandlerAdapter{

    @Override
    public void reduce(HandlerContext ctx, Page page) {
        System.out.println(page.getResult());
    }
}
