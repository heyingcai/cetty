package test.net;

import com.bronson.cetty.core.net.HttpClientGenerator;
import com.bronson.cetty.core.Payload;
import com.bronson.cetty.core.net.SyncHttpClientGenerator;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author heyingcai
 */
public class SyncHttpClientTest {
    public static void main(String[] args) {
        HttpClientGenerator<CloseableHttpClient> httpClientHttpClientGenerator = new SyncHttpClientGenerator();
        Payload payload = new Payload();
        CloseableHttpClient client = httpClientHttpClientGenerator.getClient(payload);

        HttpRequestBase httpGet = new HttpGet("http://www.baidu.com");

        try {
            CloseableHttpResponse execute = client.execute(httpGet);

            System.out.println(EntityUtils.toString(execute.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
