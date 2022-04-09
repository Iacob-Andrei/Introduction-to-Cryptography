import java.security.SecureRandom;

public class RC4 {

    private int[][] key;
    private int[][] s = new int[256][8];

    public RC4() {

        SecureRandom rd = new SecureRandom();
        int[][] key = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                key[i][j] = rd.nextInt(2);  // 0 sau 1
            }
        }

        for(int i = 0; i < s.length; i++) {
            s[i] = toBinary(i);
        }

        int j = 0;
        for(int i = 0; i < 256; i++) {

            int stateByte = toDecimal(s[i]);
            int keyByte = toDecimal(key[i % 8]);
            j = (j + stateByte + keyByte) % 256;
            int[] temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
    }


    public int[] PRGA() {

        int i = 0, j = 0;
        int[] secondByte = new int[8];

        for( int k = 0 ; k < 256 ; k++){
            i = (i + 1) % 256;
            j = (j + toDecimal(s[i])) % 256;

            int[] copy = s[i];
            s[i] = s[j];
            s[j] = copy;

            int M = ( toDecimal(s[i]) + toDecimal(s[j]) ) % 256;

            if( k == 1 )
                secondByte = s[M];

            /*
            for( int index = 0 ; index < 8 ; index++ )
                System.out.print(s[M][index]);
            System.out.println();
             */
        }

        return secondByte;
    }

    public int[] toBinary(int nr) {
        int[] binNum = new int[8];
        int numberToConvert = nr;
        for(int i = 7; i >= 0; i--)
        {
            binNum[i] = numberToConvert % 2;
            numberToConvert /= 2;
        }
        return binNum;
    }

    public int toDecimal(int[] nr){
        int decNum = 0;
        int power = 1;
        for(int i = 7; i >= 0; i--)
        {
            decNum += nr[i] * power;
            power *= 2;
        }
        return decNum;
    }

}
