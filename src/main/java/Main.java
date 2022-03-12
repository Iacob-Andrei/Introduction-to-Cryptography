import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Main {

    static int lengthOfPrime = 64;
    static int lengthOfRandom = 1_000_000;
    static BigInteger seed;
    static BigInteger n;
    static StringBuilder randomNumberBBS;
    static StringBuilder randomNumberJacobi;

    static void setup(){

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

    static void BBS(){

        randomNumberBBS = new StringBuilder();
        long numberOfOne  = 0;
        long numberOfZero = 0;

        for( long position = 0 ; position < lengthOfRandom ; position++ ){

            seed = seed.multiply(seed);
            seed = seed.mod(n);

            BigInteger parity = seed.mod(BigInteger.valueOf(2));
            randomNumberBBS.append(parity);

            if( parity.toString().equals("0"))
                numberOfZero++;
            else
                numberOfOne++;
        }

        System.out.println("Blum-Blum-Shub pseudo-random numbers is: ");
        //System.out.println(randomNumber);

        System.out.println("\nNumber of 0: " + numberOfZero + " | Number of 1: " + numberOfOne);

        List<Integer> compressed = LZW.encode(randomNumberBBS.toString());

        System.out.println("Compressed pseudo-random number length is: " + compressed.size() + " || Original pseudo-random number length is: " + randomNumberBBS.length());
        System.out.println("Compression ration is: " + (double)compressed.size()/lengthOfRandom);
    }

    static String jacobiSymbol(BigInteger a, BigInteger n){

        BigInteger b;
        b = a.mod(n);
        int s = 1;

        while( b.compareTo( BigInteger.valueOf(2)) >= 0 ){

            while( b.mod( BigInteger.valueOf(4) ).compareTo( BigInteger.valueOf(0) ) == 0  ){
                b = b.divide( BigInteger.valueOf(4) );
            }

            if( b.mod( BigInteger.valueOf(2) ).compareTo( BigInteger.valueOf(0) ) == 0 ){

                if( n.mod( BigInteger.valueOf(8) ).compareTo( BigInteger.valueOf(3) ) == 0 || n.mod( BigInteger.valueOf(8) ).compareTo( BigInteger.valueOf(5) ) == 0 )
                    s *= -1;

                b = b.divide( BigInteger.valueOf(2) );
            }

            if( b.compareTo( BigInteger.valueOf(1) ) == 0 ){
                break;
            }

            // TO DO - LINE 9 - 11
            if( b.mod( BigInteger.valueOf(4) ).compareTo( BigInteger.valueOf(3) ) == 0 )
                if( n.mod( BigInteger.valueOf(4) ).compareTo( BigInteger.valueOf(3) ) == 0 )
                    s *= -1;

            BigInteger copyOfB = b;
            b = n.mod(b);
            n = copyOfB;
        }

        b = b.multiply( BigInteger.valueOf(s) );
        return b.toString();
    }

    static void jacobiPseudoRandom(){

        randomNumberJacobi = new StringBuilder();
        long numberOfOne  = 0;
        long numberOfZero = 0;

        for( long position = 0 ; position < 1000000 ; position++ ){

            String jacobi = jacobiSymbol( seed, n );

            if( jacobi.compareTo("1") == 0 ){
                randomNumberJacobi.append("1");
                numberOfOne++;
            }else{
                randomNumberJacobi.append("0");
                numberOfZero++;
            }

            seed = seed.add(BigInteger.valueOf(1));
        }

        //System.out.println("Jacobi pseudo-random number is:");
        System.out.println("\nNumber of 0: " + numberOfZero + " | Number of 1: " + numberOfOne);
    }

    public static void main(String[] args) {

        setup();
        //BBS();
        //jacobiPseudoRandom();

        BigInteger nr1 = new BigInteger("27129");
        BigInteger nr2 = new BigInteger("1424743");
        System.out.println( jacobiSymbol(nr1, nr2));
    }
}
