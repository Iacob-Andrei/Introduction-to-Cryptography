import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BBS {

    static int lengthOfPrime;
    static int lengthOfRandom;
    static BigInteger seed;
    static BigInteger n;
    static StringBuilder randomNumber;


    static void setup(){

        Scanner myScanner = new Scanner(System.in);
        System.out.println("length for prime numbers (bytes): ");
        lengthOfPrime  = myScanner.nextInt();
        System.out.println("length for the pseudo-random number (bits): ");
        lengthOfRandom = myScanner.nextInt();

        BigInteger parity;
        BigInteger p;
        BigInteger q;

        do {
            p = new BigInteger(lengthOfPrime, 1, new Random());
            parity = p.mod(BigInteger.valueOf(4));
        }while(!parity.toString().equals("3"));

        do {
            q = new BigInteger(lengthOfPrime, 1, new Random());
            parity = p.mod(BigInteger.valueOf(4));
        }while(!parity.toString().equals("3"));

        n = p.multiply(p);

        System.out.println("n = " + p + " * " + q + " = " + n );

        Date date = new Date();
        seed = BigInteger.valueOf( date.getTime() );
        seed = seed.multiply(seed);
        seed = seed.mod(n);

        System.out.println("The seed is: " + seed);
    }

    static void generateRandomPseudoNumber(){

        randomNumber = new StringBuilder();
        long numberOfOne  = 0;
        long numberOfZero = 0;

        for( long position = 0 ; position < lengthOfRandom ; position++ ){

            seed = seed.multiply(seed);
            seed = seed.mod(n);

            BigInteger parity = seed.mod(BigInteger.valueOf(2));
            randomNumber.append(parity);

            if( parity.toString().equals("0"))
                numberOfZero++;
            else
                numberOfOne++;
        }

        //System.out.println("Generated pseudo-random number is" + randomNumber);

        System.out.println("\nNumber of 0: " + numberOfZero + " | Number of 1: " + numberOfOne);

        List<Integer> compressed = LZW.encode(randomNumber.toString());

        System.out.println("Compressed pseudo-random number length is: " + compressed.size() + " || Original pseudo-random number length is: " + randomNumber.length());
        System.out.println("Compression ration is: " + (double)compressed.size()/lengthOfRandom);
    }

    public static void main(String[] args) {

        setup();
        generateRandomPseudoNumber();
    }
}
