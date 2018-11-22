package com.jibug.cetty.core.net;

import com.jibug.cetty.core.Payload;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
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

        final HttpGet[] requests = new HttpGet[]{
                new HttpGet("http://www.apache.org/"),
                new HttpGet("http://www.baidu.com/"),
                new HttpGet("http://www.oschina.net/")
        };

        for(final HttpGet request: requests){
            client.execute(request, new FutureCallback(){
                @Override
                public void completed(Object obj) {
                    final HttpResponse response = (HttpResponse)obj;
                    System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                }

                @Override
                public void failed(Exception excptn) {
                    System.out.println(request.getRequestLine() + "->" + excptn);
                }

                @Override
                public void cancelled() {
                    System.out.println(request.getRequestLine() + "cancelled");
                }
            });
        }


//        for (int i = 0; i < 2; i++) {
//            HttpRequestBase httpGet = new HttpGet("http://www.baidu.com");
//            client.execute(httpGet, new Back());
//            System.out.println("index :" + i);
//            httpGet.releaseConnection();
//        }

//        client.execute(httpGet, new Back());
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
