public class Main {

    public static void main(String[] args) {

        String plaintext = "ana are mere";
        byte[] bytes = plaintext.getBytes();
        String result = MD5.toHexString(MD5.computeMD5(bytes));
        System.out.println(plaintext + " ==> " + result);

        String newPlainText = "bna are mere";
        byte[] newBytes = newPlainText.getBytes();
        String newResult = MD5.toHexString(MD5.computeMD5(newBytes));
        System.out.println( newPlainText + " ==> " + newResult);

        int count = 0;
        for( int i = 0 ; i < result.length() ; i++ ){
            if( result.charAt(i) != newResult.charAt(i) )
                count++;
        }

        System.out.println("Hamming distance is: " + count);
    }
}
