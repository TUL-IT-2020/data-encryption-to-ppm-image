package Tools;

import static Tools.ByteTools.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
            output = Tools.ByteTools.nextInt(Bytes);
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
        }
        for (String s : strs) {
            String output = nextString(Bytes, s.getBytes().length);
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
        System.out.format("Int to bin: \t\t");
        int len = 8;
        String string = "";
        for (int i = 0; i < 256; i++) {
            string = int2BinString(i, len);
            //System.out.format(" %s\n", string);
            assertEquals(len, string.length());
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void nextInt() {
        System.out.format("next int: \t\t");
        int size = 256;
        int[] ints;
        int intRead;
        List<Byte> Bytes = new ArrayList();
        Random rand = new Random();
        // generte longs
        ints = rand.ints(size).toArray();
        
        // write longs
        for (int i = 0; i < size; i++) {
            add2List(Bytes, int2Bytes(ints[i]));
        }
        
        // read longs
        for (int i = 0; i < size; i++) {
            intRead = Tools.ByteTools.nextInt(Bytes);
            assertEquals(ints[i], intRead);
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void nextLong() {
        System.out.format("next long: \t\t");
        int size = 256;
        long[] longs;
        long longRead;
        List<Byte> Bytes = new ArrayList();
        Random rand = new Random();
        // generte longs
        longs = rand.longs(size).toArray();
        
        // write longs
        for (int i = 0; i < size; i++) {
            add2List(Bytes, long2Bytes(longs[i]));
        }
        
        // read longs
        for (int i = 0; i < size; i++) {
            longRead = Tools.ByteTools.nextLong(Bytes);
            assertEquals(longs[i], longRead);
        }
        System.out.format("Done\n");
    }
    
}
