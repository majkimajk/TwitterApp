import org.apache.commons.codec.DecoderException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.TreeMap;


/**
 * Created by michalbaran on 21/09/16.
 */
public class TwitPost {

public TwitPost() throws DecoderException, NoSuchAlgorithmException, InvalidKeyException {

    String status = "test twit";
    String method = "POST";
    String url = "https://api.twitter.com/1.1/statuses/update.json";
    String host = "api.twitter.com";
    String agent = "TwitTest v. 0.0.1";
    String include_entities = "true";
    String consKey = "Gf59eZi1jLpOyf71dPjaGOIyg";
    String nonce = NonceMaker.getNonce(NonceMaker.getList());
    String signatureMethod="HMAC-SHA1";
    String timestamp = String.valueOf(System.currentTimeMillis()/1000);
    String oAuthToken="726552394843168768-X1iJR5SHAc07iZvSr2zf1vVYFRzyDgf";
    String version="1.0";
    HttpPost post = new HttpPost(url);

    //create the signature


    TreeMap<String, String> preSignList = new TreeMap<>();
    preSignList.put("status", status);
    preSignList.put("include_entities", include_entities);
    preSignList.put("oauth_consumer_key", consKey);
    preSignList.put("oauth_nonce", nonce);
    preSignList.put("oauth_signature_method", signatureMethod);
    preSignList.put("oauth_timestamp", timestamp);
    preSignList.put("oauth_token", oAuthToken);
    preSignList.put("oauth_version", version);


    //percent encode each key and value and sort them
    TreeMap<String, String> encSignList = new TreeMap<>();
    preSignList.forEach((key, value) -> encSignList.put(URLEncoder.encodeUTF8(key), URLEncoder.encodeUTF8(value)));
    //encSignList.forEach((key, value) -> System.out.println(key + " " + value));

    //create a string out of keys and values
    StringBuilder sb = new StringBuilder();
    encSignList.forEach((key, value) -> sb.append(key).append("=").append(value).append("&"));
    sb.deleteCharAt(sb.length() - 1);
    String preSignature = sb.toString();
    System.out.println(sb.toString());
    System.out.println();

    StringBuilder signatureBuilder = new StringBuilder();

    signatureBuilder.append(method).append("&").append(URLEncoder.encodeUTF8(url)).append("&").append(URLEncoder.encodeUTF8(preSignature));
    String signatureBase = signatureBuilder.toString();

    System.out.println(signatureBase);
    System.out.println();

    // creating a signing key


    String consSecret = "TScz6noTduE1HgJ72Fys3CjCKZV1ER4Bk6cy16WCto6eaQ1jHH";
    String oAuthSecret = "WqF3T7khwEfpQRW8E2hh3eyVEe8a1GjC8gjKfAPAPXPVY";

    String signingKey = URLEncoder.encodeUTF8(consSecret) + "&" + URLEncoder.encodeUTF8(oAuthSecret);
    System.out.println(signingKey);

    // finally creating the signature by using HMAC SHA1 algorithm

    SecretKeySpec keySpec = new SecretKeySpec(consKey.getBytes(), "HmacSHA1");
    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(keySpec);


    byte[] signatureBytes = mac.doFinal(signatureBase.getBytes());

    String signature = Base64.getEncoder().encodeToString(signatureBytes);

   //System.out.println(signature);

//Create the header string for POST request



    StringBuilder hb = new StringBuilder();
    hb.append("OAuth ");
    hb.append(URLEncoder.encodeUTF8("oauth_consumer_key")).append("=\"").append(URLEncoder.encodeUTF8(consKey)).append("\", ");
    hb.append(URLEncoder.encodeUTF8("oauth_nonce")).append("=\"").append(URLEncoder.encodeUTF8(nonce)).append("\", ");
    hb.append(URLEncoder.encodeUTF8("oauth_signature")).append("=\"").append(URLEncoder.encodeUTF8(signature)).append("\", ");
    hb.append(URLEncoder.encodeUTF8("oauth_signature_method")).append("=\"").append(URLEncoder.encodeUTF8(signatureMethod)).append("\", ");
    hb.append(URLEncoder.encodeUTF8("oauth_timestamp")).append("=\"").append(URLEncoder.encodeUTF8(timestamp)).append("\", ");
    hb.append(URLEncoder.encodeUTF8("oauth_token")).append("=\"").append(URLEncoder.encodeUTF8(oAuthToken)).append("\", ");
    hb.append(URLEncoder.encodeUTF8("oauth_version")).append("=\"").append(URLEncoder.encodeUTF8(version)).append("\"");
    String header = hb.toString();
    //System.out.println(header);

    post.setHeader("Host", host);
    post.setHeader("Accept", "*/*");
    post.setHeader("Connection", "close");
    post.setHeader("User-Agent", agent);
    post.setHeader("Content-Type", "application/x-www-form-urlencoded");

   post.setHeader("Authorization", header);
/*        post.setHeader("Content-Length", "29");
        post.setHeader("Accept-Encoding", "gzip");*/

  try {

        HttpEntity entity = new StringEntity("status="+URLEncoder.encodeUTF8(status));
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




    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}

}
