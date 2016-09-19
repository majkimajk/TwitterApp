import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by michalbaran on 12/09/16.
 */

public class TwitTest{

    private final String AGENT = "TwitTest v. 0.0.1";


    public static void main(String[] args){

        TwitTest Twit = new TwitTest();
        Twit.obtainBearerToken();
        Twit.getResponse();


    }

    private void getResponse() {
        String url = "https://api.twitter.com/1.1/statuses/user_timeline.json?count=100&screen_name=twitterapi";

        HttpGet get = new HttpGet(url);
        get.setHeader("Host", "api.twitter.com");;
        get.setHeader("User-Agent", AGENT);
       // get.setHeader("Accept-Encoding", "gzip");
        //obtaining a bearer token
       // BearerTokenGen btg = new BearerTokenGen();

        get.setHeader("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAALuSwwAAAAAAo%2F62MB7x3KvhSt0zsaDSjBMaacA%3D80s5xDiqAAdIRfnPbI57JqPkAuAlBpEHpRfcXLYlyi2vgkER3y");


        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse response = httpClient.execute(get);
            System.out.println(response.getStatusLine().getStatusCode());
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private String obtainBearerToken() {

        String url = "https://api.twitter.com/oauth2/token";
        String bearerToken = "";
        // HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);



        post.setHeader("Host", "api.twitter.com");;
        post.setHeader("User-Agent", AGENT);
        //obtaining a bearer token
        BearerTokenGen btg = new BearerTokenGen();

        post.setHeader("Authorization", "Basic " + btg.getEncKeys());
        post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
/*        post.setHeader("Content-Length", "29");
        post.setHeader("Accept-Encoding", "gzip");*/

        try {
            HttpEntity entity = new StringEntity("grant_type=client_credentials");
            post.setEntity(entity);
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse response = httpClient.execute(post);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result);
            bearerToken = result.toString();



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bearerToken;
    }

}
