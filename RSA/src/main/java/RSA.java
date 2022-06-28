import java.math.BigInteger;
import java.util.Random;

public class RSA {

    public static void main(String[] args) {

        BigInteger p = new BigInteger(1024, 1, new Random());
        BigInteger q = new BigInteger(1024, 1, new Random());

        BigInteger n = p.multiply(q);

        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // 1 < e < phi(n), gcd(e,Phi) = 1
        BigInteger e = genE(phi);

        // d ≡ e^(-1) (mod Phi(n))
        BigInteger d = extEuclid(e, phi)[1];

        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("n: " + n);
        System.out.println("Phi: " + phi);
        System.out.println("e: " + e);
        System.out.println("d: " + d);

        String message = "ANA ARE MERE";
        System.out.println("Original message: " + message);

        BigInteger cipherMessage = stringCipher(message);
        System.out.println("Ciphered: " + cipherMessage);

        // ENCRYPT USING enc(m) = m ^ e mod n
        BigInteger encrypted = cipherMessage.modPow(e,n);
        System.out.println("Encrypted: " + encrypted);


        //DECRYPT USING enc(c) = y ^ d mod phi(n)
        //              d = e ^ (-1) mod phi(n)
        BigInteger decrypted = encrypted.modPow(d,n);
        System.out.println("Decrypted: " + decrypted);

        String restoredMessage = cipherToString(decrypted);
        System.out.println("Original message: " + restoredMessage);


        //CHINESE REMAINDER THEOREM DECRYPT
        BigInteger xp = encrypted.mod(p);
        BigInteger xpExponent = d.mod(p.subtract(BigInteger.ONE));
        xp = xp.modPow(xpExponent, xpExponent.add(BigInteger.ONE));
        System.out.println(xp);

        BigInteger xq = encrypted.mod(q);
        BigInteger xqExponent = d.mod(q.subtract(BigInteger.ONE));
        xq = xq.modPow(xqExponent, xqExponent.add(BigInteger.ONE));
        System.out.println(xq);

        // x ≡ xp mod p
        // x ≡ xq mod q
        System.out.println( CRT(xp, p, xq, q) );
    }

    public static BigInteger CRT(BigInteger xp, BigInteger p, BigInteger xq, BigInteger q){

        BigInteger prod = p.multiply(q);
        // find x1 in: p * x1 ≡ xq mod q
        BigInteger x1 = linearCongruence(p, xq, q);

        // find x2 in: q * x2 ≡ xp mod p
        BigInteger x2 = linearCongruence(q, xp, p);

        // x = ( p*x1 + q*x2 ) mod p*q
        return p.multiply(x1).add(q.multiply(x2)).mod(prod);
    }

    /**
     * @return solution for a*x ≡ b mod n
     */
    public static BigInteger linearCongruence(BigInteger A, BigInteger B, BigInteger N) {

        A = A.mod(N);
        B = B.mod(N);
        BigInteger u;

        BigInteger[] vals = ExtendedEuclidAlgo(A, N);
        BigInteger d = vals[0];
        u = vals[1];

        BigInteger x0 = u.multiply(B.divide(d)).mod(N);
        if ( x0.compareTo(BigInteger.ZERO) < 0)
            x0 = x0.add(N);

        return x0;
    }

    public static BigInteger[] ExtendedEuclidAlgo(BigInteger a, BigInteger b) {

        if (a.equals(BigInteger.ZERO)) {
            return new BigInteger[]{b, BigInteger.ZERO, BigInteger.ONE};
        }
        else {

            BigInteger x1;
            BigInteger y1;
            BigInteger[] gcdy = ExtendedEuclidAlgo(b.mod(a), a);
            BigInteger gcd = gcdy[0];
            x1 = gcdy[1];
            y1 = gcdy[2];

            BigInteger y = new BigInteger(String.valueOf(x1));
            BigInteger x = y1.subtract(b.divide(a).multiply(x1));

            return new BigInteger[] {gcd, x, y};
        }
    }



    /**
     * Takes a string and converts each character to an ASCII decimal value
     * Returns BigInteger
     */
    public static BigInteger stringCipher(String message) {

        message = message.toUpperCase();
        StringBuilder cipherString = new StringBuilder();
        for( int i = 0 ; i < message.length(); i++) {
            int ch = message.charAt(i);
            cipherString.append(ch);
        }
        return new BigInteger(cipherString.toString());
    }

    /**
     * Takes a BigInteger that is ciphered and converts it back to plain text
     *	returns a String
     */
    public static String cipherToString(BigInteger message) {

        String cipherString = message.toString();
        StringBuilder output = new StringBuilder();

        for (int i = 0 ; i < cipherString.length(); i+=2) {
            int temp = Integer.parseInt(cipherString.substring(i, i + 2));
            char ch = (char) temp;
            output.append(ch);
        }
        return output.toString();
    }

    /**
     * Recursive implementation of Euclidian algorithm to find greatest common denominator
     */
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        } else {
            return gcd(b, a.mod(b));
        }
    }

    /** Recursive EXTENDED Euclidean algorithm, (ax + by = gcd(a,b))
     * and finds the multiplicative inverse which is the solution to ax ≡ 1 (mod m)
     * returns [d, p, q] where d = gcd(a,b) and ap + bq = d
     */
    public static BigInteger[] extEuclid(BigInteger a, BigInteger b) {

        if (b.equals(BigInteger.ZERO)) return new BigInteger[] {
                a, BigInteger.ONE, BigInteger.ZERO
        };

        BigInteger[] vals = extEuclid(b, a.mod(b));
        BigInteger d = vals[0];
        BigInteger p = vals[2];
        BigInteger q = vals[1].subtract(a.divide(b).multiply(vals[2]));

        return new BigInteger[] {d, p, q};
    }

    /**
     * generate e by finding a Phi such that they are co primes (gcd = 1)
     */
    public static BigInteger genE(BigInteger phi) {

        Random rand = new Random();
        BigInteger e ;
        do {
            e = new BigInteger(32, rand);
            while (e.min(phi).equals(phi)) { // while phi is smaller than e, look for a new e
                e = new BigInteger(32, rand);
            }
        } while (!gcd(e, phi).equals(BigInteger.ONE)); // if gcd(e,phi) isnt 1 then stay in loop

        return e;
    }
}