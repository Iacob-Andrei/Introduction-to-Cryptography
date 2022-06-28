import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BirthdayAttack {

    static public Set<String> generated = new HashSet<>();
    static public String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    static public String generateRandomWord(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();


        for(int poz = 0 ; poz < 50 ; poz++ ){
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return sb.toString();
    }

    public static void main(String[] args) {

        int count = 0;

        while (true) {
            count++;
            String word = generateRandomWord();
            byte[] bytes = word.getBytes();
            String result = MD5.toHexString(MD5.computeMD5(bytes)).substring(0,4);

            if( generated.contains(result) ) {
                System.out.println("A collision was found after " + count );
                break;
            }

            generated.add(result);
        }
    }
}
