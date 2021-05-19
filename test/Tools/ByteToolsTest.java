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
    
    public ByteToolsTest() {
    }

    @Test
    public void int2ByteAndBack() {
        int[] numbers = {0, 1, -1, 42, 1024};
        int output;
        List<Byte> Bytes = new ArrayList();
        for (int number : numbers) {
            add2List(Bytes, int2Bytes(number));
            output = nextInt(Bytes);
            Assert.assertEquals(number + "!=" + output, number, output);
            //System.out.format("%d == %d\n", number, output);
        }
    }
    
    @Test
    public void string2ByteAndBack() {
        String text = "Ahoj! Tohle je zkouška žščř";
        String[] strs = text.split(" ");
        List<Byte> Bytes = new ArrayList();
        for (String s : strs) {
            add2List(Bytes, s.getBytes());
            String output = nextString(Bytes, s.length());
            System.out.format("%s == %s\n", s, output);
        }
    }
    
    @Test
    public void nthBitFromLeftTest() {
        int[][] data = {
            {170, 1,0,1,0, 1,0,1,0}
        };
        byte bit;
        for (int[] input : data) {
            for (int i = 0; i < 8; i++) {
                bit = nthBitFromLeft((byte)input[0], i);
                //System.out.format("new: %8.8s\t", Integer.toString((channel & ~mask) | nthBit & 0xFF,2));
                System.out.format("%d -> %d index\n", bit, i);
                assertEquals(input[i+1], (int)bit);
            }
        }
    }
    
    @Test
    public void int2bin() {
        int len = 8;
        for (int i = 0; i < 256; i++) {
            //System.out.format(" %s\n", int2BinString(i, len));
        }
    }
}
