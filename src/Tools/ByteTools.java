package Tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 *
 * @author pytel
 */
public class ByteTools {
    
    public static final int BYTE_LENGHT = 8;
    public static final int INT_LENGHT = 8*4;
    
    public static void add2List (List list, byte[] bytes) {
        for (byte B : bytes) {
            list.add(B);
        }
    }
    
    public static char byte2char (byte B) {
        return (char)(B & 0xFF);
    }
    
    public static byte nthBitFromRight (int B, int index) {
        return (byte)((B >> index) & 1);
    }
    
    public static byte nthBitFromLeft (byte B, int index) {
        return (byte)((B >> 7-index) & 1);
    }
    
    public static byte[] int2Bytes (int number) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) ((number >> 24) & 0xFF);
        byteArray[1] = (byte) ((number >> 16) & 0xFF);
        byteArray[2] = (byte) ((number >> 8) & 0xFF);
        byteArray[3] = (byte) (number & 0xFF);
        return byteArray;
    }
    
    public static int byteArrayToInt(byte [] b) { 
        return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) + (b[3] & 0xFF); 
    }
    
    public static byte[] int2BytesLE(int myInteger) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
    }

    public static int byteArrayToIntLE(byte[] byteBarray) {
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static byte[] int2BytesBE(int myInteger) {
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(myInteger).array();
    }

    public static int byteArrayToIntBE(byte[] byteBarray) {
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.BIG_ENDIAN).getInt();
    }
    
    public static int nextInt (List<Byte> Bytes) {
        byte[] arrayInt = new byte[4];
        for (int i = 0; i < 4; i++) {
            arrayInt[i] = Bytes.remove(0);
        }
        return byteArrayToInt(arrayInt);
    }
    
    public static String nextString (List<Byte> Bytes, int len) {
        byte[] arrayString = new byte[len];
        for (int i = 0; i < len; i++) {
            arrayString[i] = Bytes.remove(0);
        }
        return new String(arrayString);
    }
}
