import java.util.ArrayList;
import java.util.Random;

/**
 * Created by michalbaran on 25/09/16.
 */
public final class NonceMaker {

    private NonceMaker(){


    }

private static ArrayList<Character> list = new ArrayList<>();

    public static ArrayList<Character> getList(){
        for (char i = 'a'; i <= 'z'; i++) {
            list.add(i);
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            list.add(i);
        }
        for (char i = '0'; i <= '9'; i++) {
            list.add(i);
        }

        return list;

    }

    public static String getNonce(ArrayList<Character> charList) {

        int j = 0;
        StringBuilder sb = new StringBuilder();
        while (j < 32) {

            int rand = new Random().nextInt(charList.size());
            sb.append(charList.get(rand));
            j++;

        }
        String nonce = sb.toString();
        return nonce;
    }




}
