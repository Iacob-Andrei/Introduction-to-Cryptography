import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LFSR {

    static int confInit;
    static String base2ConfInit;
    // pentanmom = x ^ 16 + x ^ 5 + x ^ 3 + x ^ 2 + 1

    public static List<Integer> setup(){

        Random random = new Random();
        confInit = random.nextInt( ((int)Math.pow(2,16) - 1) - 1 ) + 1;

        return writeInBase2();
    }

    public static List<Integer> writeInBase2(){

        List<Integer> sequence = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
        base2ConfInit = Integer.toBinaryString(confInit);
        int dif = 16 -  base2ConfInit.length();

        for( int pos = 0 ; pos < base2ConfInit.length(); pos++ ){
            sequence.set(pos+dif, (int)base2ConfInit.charAt(pos) - 48);
        }

        System.out.println(confInit);
        System.out.println(base2ConfInit);
        System.out.println(sequence);

        return sequence;
    }

    public static List<Integer> shift(List<Integer> sequence, Integer newByte){

        for( int pos = 15; pos > 0; pos--){
            sequence.set(pos, sequence.get(pos-1));
        }
        sequence.set(0, newByte);
        return sequence;
    }

    public static boolean compare(List<Integer> sequence, List<Integer> copy){

        for( int poz = 0 ; poz < 16 ; poz++ )
            if( sequence.get(poz).compareTo(copy.get(poz)) != 0  )
                return false;

        return true;
    }

    public static void generate(List<Integer> sequence){

        List<Integer> copy = new ArrayList<>(sequence);
        int length = 0;

        do {
            int newByte = (sequence.get(15) + sequence.get(4) + sequence.get(2) + sequence.get(1)) % 2;
            shift(sequence, newByte);
            System.out.print(newByte);
            length++;
        } while (!compare(sequence, copy));

        System.out.println("\n" + length);
    }

}
