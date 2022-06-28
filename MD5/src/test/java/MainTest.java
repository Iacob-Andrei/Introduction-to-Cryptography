import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    public void test1(){
        String plainText = "abc";
        assertEquals("900150983cd24fb0d6963f7d28e17f72".toUpperCase(), MD5.toHexString(MD5.computeMD5(plainText.getBytes())) );
    }

    @Test
    public void test2(){
        String plainText = " ";
        assertEquals("d41d8cd98f00b204e9800998ecf8427e".toUpperCase(), MD5.toHexString(MD5.computeMD5(plainText.getBytes())) );
    }

    @Test
    public void test3(){
        String plainText = "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu";
        assertEquals("03dd8807a93175fb062dfb55dc7d359c".toUpperCase(), MD5.toHexString(MD5.computeMD5(plainText.getBytes())) );
    }

}