package Tools;

import static Tools.ByteTools.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class ByteToolsTest {
    
    @Test
    public void int2ByteAndBack() {
        System.out.format("int2ByteAndBack: \t");
        int[] numbers = {0, 1, -1, 42, 1024};
        int output;
        List<Byte> Bytes = new ArrayList();
        for (int number : numbers) {
            add2List(Bytes, int2Bytes(number));
            output = nextInt(Bytes);
            Assert.assertEquals(number + "!=" + output, number, output);
            //System.out.format("%d == %d\n", number, output);
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void string2ByteAndBack() {
        System.out.format("string2ByteAndBack: \t");
        String text = "Ahoj! Tohle je zkouška žščř";
        String[] strs = text.split(" ");
        List<Byte> Bytes = new ArrayList();
        for (String s : strs) {
            add2List(Bytes, s.getBytes());
            String output = nextString(Bytes, s.length());
            System.out.format("%s == %s\n", s, output);
            assert s.compareTo(output) == 0 : "ERROR: strings not same!";
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void nthBitFromLeftTest() {
        System.out.format("nthBitFromLeftTest: \t");
        int[][] data = {
            {170, 1,0,1,0, 1,0,1,0}
        };
        byte bit;
        for (int[] input : data) {
            for (int i = 0; i < 8; i++) {
                bit = nthBitFromLeft((byte)input[0], i);
                //System.out.format("new: %8.8s\t", Integer.toString((channel & ~mask) | nthBit & 0xFF,2));
                //System.out.format("%d -> %d index\n", bit, i);
                assertEquals(input[i+1], (int)bit);
            }
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void int2bin() {
        int len = 8;
        for (int i = 0; i < 256; i++) {
            //System.out.format(" %s\n", int2BinString(i, len));
        }
    }
}
