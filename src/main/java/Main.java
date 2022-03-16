import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Main {

    static int lengthOfPrime = 512;
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
        System.out.println(randomNumberBBS);

        System.out.println("\nNumber of 0: " + numberOfZero + " | Number of 1: " + numberOfOne);

        List<Integer> compressed = LZW.encode(randomNumberBBS.toString());

        System.out.println("Compressed pseudo-random number length is: " + compressed.size() + " || Original pseudo-random number length is: " + randomNumberBBS.length());
        System.out.println("Compression ration is: " + (double)compressed.size()/lengthOfRandom);

        StringBuilder oneZero = new StringBuilder();
        for( int index = 0 ; index < 1_000_000 ; index++ )
            if( index % 2 == 0 )
                oneZero.append("1");
        else
            oneZero.append("0");

        List<Integer> compressedOneZero = LZW.encode(oneZero.toString());
        System.out.println("Compressed zero-one number length is: " + compressedOneZero.size() + " || Original zero-one number length is: " + oneZero.length());
        System.out.println("Compression ration is: " + (double)compressedOneZero.size()/lengthOfRandom);
    }

    static String jacobiSymbol(BigInteger a, BigInteger n){

        BigInteger b = a.mod(n);
        BigInteger c = n;
        int s = 1;

        while( b.compareTo(BigInteger.valueOf(2)) >= 0 ){

            while( b.mod( BigInteger.valueOf(4) ).compareTo( BigInteger.valueOf(0) ) == 0 ){
                b = b.divide( BigInteger.valueOf(4) );
            }

            if( b.mod( BigInteger.valueOf(2) ).compareTo( BigInteger.valueOf(0) ) == 0 ){

                BigInteger cMod8 = c.mod( BigInteger.valueOf(8) );

                if( cMod8.compareTo( BigInteger.valueOf(3) ) == 0 || cMod8.compareTo( BigInteger.valueOf(5) ) == 0) {
                    s = -s;
                }
                b = b.divide( BigInteger.valueOf(2) );
            }

            if( b.compareTo( BigInteger.valueOf(1) ) == 0 )
                break;

            BigInteger bMod4 = b.mod( BigInteger.valueOf(4) );
            BigInteger cMod4 = c.mod( BigInteger.valueOf(4) );

            if( bMod4.compareTo( BigInteger.valueOf(3) ) == 0 &&  cMod4.compareTo( BigInteger.valueOf(3) ) == 0 ){
                s = -s;
            }

            BigInteger copyOfB = b;
            b = c.mod(b);
            c = copyOfB;
        }

        b = b.multiply( BigInteger.valueOf(s) );
        return b.toString();
    }

    static void jacobiPseudoRandom(){

        System.out.println("Jacobi pseudo-random number is:");

        randomNumberJacobi = new StringBuilder();
        long numberOfOne  = 0;
        long numberOfZero = 0;

        for( long position = 0 ; position < 1_000_000 ; position++ ){

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

        System.out.println(randomNumberJacobi);
        System.out.println();
        System.out.println("\nNumber of 0: " + numberOfZero + " | Number of 1: " + numberOfOne);
    }

    public static void main(String[] args) {

        setup();
        //BBS();
        jacobiPseudoRandom();

        /*
        BigInteger nr1 = new BigInteger("452342");
        BigInteger nr2 = new BigInteger("5423531");
        System.out.println( jacobiSymbol(nr1, nr2));
         */

    }
}
