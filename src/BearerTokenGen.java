import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by michalbaran on 12/09/16.
 */
public class BearerTokenGen {

    private final String consKey = "Gf59eZi1jLpOyf71dPjaGOIyg";
    private final String consSecret = "TScz6noTduE1HgJ72Fys3CjCKZV1ER4Bk6cy16WCto6eaQ1jHH";
    private String encKeys = null;

    public BearerTokenGen() {
        // encode keys according to rfc 1738

        String encConsKey = URLEncoder.encodeUTF8(consKey);
        String ensConsSecret = URLEncoder.encodeUTF8(consSecret);

        //System.out.println(encConsKey);
        //System.out.println(ensConsSecret);

        String concKeys = encConsKey + ":" + ensConsSecret;
        //System.out.println(concKeys);

        // base64 encode

        byte[] concByteKeys = concKeys.getBytes(StandardCharsets.UTF_8);
        encKeys = Base64.getEncoder().encodeToString(concByteKeys);
        //System.out.println(encKeys);
    }

    public String getEncKeys() {
        return encKeys;
    }
}
