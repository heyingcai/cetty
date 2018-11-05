package test.net;

import com.bronson.cetty.core.net.AsyncHttpClientGenerator;
import com.bronson.cetty.core.net.HttpClientGenerator;
import com.bronson.cetty.core.net.Payload;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author heyingcai
 */
public class AsyncHttpClientTest {

    public static void main(String[] args) {
        HttpClientGenerator<CloseableHttpAsyncClient> asyncHttpClientGenerator = new AsyncHttpClientGenerator();
        Payload payload = new Payload();
        CloseableHttpAsyncClient client = asyncHttpClientGenerator.getClient(payload);

        client.start();

        HttpRequestBase httpGet = new HttpGet("http://www.baidu.com");


        client.execute(httpGet,new Back());
    }

    static class Back implements FutureCallback<HttpResponse> {

        @Override
        public void completed(HttpResponse httpResponse) {
            try {
                System.out.println(EntityUtils.toString(httpResponse.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Exception e) {
            System.err.println("error:" + e);
        }

        @Override
        public void cancelled() {

        }
    }
}
