import java.util.List;

public class Main {


    public static void main(String[] args) {

        BBSJacobi.setup();

        BBSJacobi.BBS();
        BBSJacobi.jacobiPseudoRandom();


        System.out.println("Generatorul LFSR:");
        List<Integer> sequence = LFSR.setup();
        long startTime = System.nanoTime();
        LFSR.generate(sequence);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println(duration + " milliseconds");


        System.out.println("Generatorul RC4:");
        double zeroCounter = 0;

        for(int k = 0; k < 10_000; k++) {

            RC4 rc4 = new RC4();
            int[] secondByte = rc4.PRGA();
            if( rc4.toDecimal(secondByte) == 0 )
                zeroCounter++;
        }

        System.out.println("Byte-ul 0 a fost generat de " + zeroCounter + " ori, cu probabilitatea " + (zeroCounter / 10_000) );
        System.out.println(" 1 / 128 = " + (1.0/128));
        System.out.println(" 1 / 256 = " + (1.0/256));
    }
}
