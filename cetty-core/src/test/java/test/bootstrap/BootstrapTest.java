package test.bootstrap;

import com.bronson.cetty.core.Bootstrap;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.handler.PageProcessHandler;

/**
 * @author heyingcai
 */
public class BootstrapTest {

    public static void main(String[] args) {
        Payload custom = Payload.custom();
        custom.setDomain("www.baidu.com").setCharset("utf-8");

        Bootstrap.
                me().
                isAsync(false).
                setThreadNum(1).
                startUrl("http://www.baidu.com").
                setPayload(custom).
                addHandler(new PageProcessHandler()).
                start();
    }
}
