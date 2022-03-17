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

        System.out.println("Value of q is: "  + q );
        System.out.println("Value of p is: "  + p );
        System.out.println("Value of n is: "  + n );

        Date date = new Date();
        seed = BigInteger.valueOf( date.getTime() );
        seed = seed.multiply(seed);
        seed = seed.mod(n);

        System.out.println("Value of seed is: " + seed);
    }

    static void printZeroOneNumberRatio(){

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

        printZeroOneNumberRatio();
    }

    static BigInteger jacobiSymbol(BigInteger a, BigInteger n) {

        BigInteger b = a.mod(n);
        BigInteger c = n;
        int s = 1;

        while (b.compareTo(BigInteger.TWO) >= 0) {

            while(b.mod(BigInteger.valueOf(4)).compareTo(BigInteger.ZERO) == 0) {
                b = b.divide(BigInteger.valueOf(4));
            }

            if(b.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {

                BigInteger cMod8 = c.mod(BigInteger.valueOf(8));
                if(cMod8.compareTo(BigInteger.valueOf(3)) == 0 || cMod8.compareTo(BigInteger.valueOf(5)) == 0) {
                    s = -s;
                    b = b.divide(BigInteger.TWO);
                }
            }
            
            if(b.compareTo(BigInteger.ONE) == 0) 
                break;
            
            BigInteger bMod4 = b.mod(BigInteger.valueOf(4));
            BigInteger cMod4 = c.mod(BigInteger.valueOf(4));
            
            if(bMod4.compareTo(BigInteger.valueOf(3)) == 0 && cMod4.compareTo(BigInteger.valueOf(3)) == 0) {
                s = -s;
            }
            
            BigInteger oldB = b;
            b = c.mod(b);
            c = oldB;
        }
        
        return b.multiply(BigInteger.valueOf(s));
    }

    static void jacobiPseudoRandom() {

        System.out.println("\n\nJacobi Pseudo Random number is:");

        randomNumberJacobi = new StringBuilder();
        int numberOfZero = 0;
        int numberOfOne = 0;
        BigInteger copyOfSeed = seed;

        for(int i = 0; i < lengthOfRandom; i++) {

            BigInteger jacobiSymbol = jacobiSymbol(copyOfSeed,n);

            if( jacobiSymbol.compareTo(BigInteger.ONE) == 0 ){
                numberOfOne++;
                randomNumberJacobi.append("1");
            }else{
                numberOfZero++;
                randomNumberJacobi.append("0");
            }

            copyOfSeed = copyOfSeed.add(BigInteger.ONE);
        }

        System.out.println(randomNumberJacobi.toString());

        System.out.println("\nNumber of 0:" + numberOfZero + " | Number of 1:" + numberOfOne);

        List<Integer> compressed = LZW.encode(randomNumberJacobi.toString());

        System.out.println("Compressed pseudo-random number length is: " + compressed.size() + " || Original pseudo-random number length is: " + randomNumberJacobi.length());
        System.out.println("Compression ration is: " + (double)compressed.size()/lengthOfRandom);

        printZeroOneNumberRatio();
    }

    public static void main(String[] args) {

        setup();
        //BBS();
        jacobiPseudoRandom();
    }
}
