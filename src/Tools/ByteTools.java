package Tools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Statit tool lib. for work with Bytes.
 * @author pytel
 */
public class ByteTools {
    
    public static final int BYTE_LENGHT = 8;
    public static final int INT_LENGHT = 8*4;
    public static final int LONG_LENGHT = 8*8;
    
    /**
     * Append bytes[] to list.
     * @param list
     * @param bytes 
     */
    public static void add2List (List<Byte> list, byte[] bytes) {
        for (byte B : bytes) {
            list.add(B);
        }
    }
    
    /**
     * Convert int number to string as binary number.
     * @param number
     * @return bin number as string
     */
    private static String toBinString (int number) {
        if (number <= 1) return String.valueOf(number);
        return toBinString(number/2) + toBinString(number%2);
    }
    
    /**
     * Convert int number to bin string of specified lenght.
     * @param n
     * @param len
     * @return bin number as string
     */
    public static String int2BinString (int n, int len) {
        String bin = toBinString(n);
        String zeros = new String(new char[len - bin.length()]);
        zeros = zeros.replace("\0", String.valueOf(0));
        return zeros + bin;
    }
    
    /**
     * @param B
     * @return byte as char
     */
    public static char byte2char (byte B) {
        return (char)(B & 0xFF);
    }
    
    /**
     * Return value of bite on index (from right) in Byte as byte.
     * @param B
     * @param index
     * @return byte
     */
    public static byte nthBitFromRight (int B, int index) {
        return (byte)((B >> index) & 1);
    }
    
    /**
     * Return value of bite on index (from left) in Byte as byte.
     * @param B
     * @param index
     * @return byte
     */
    public static byte nthBitFromLeft (byte B, int index) {
        return (byte)((B >> 7-index) & 1);
    }
    
    /**
     * Convert int to byte[4] array.
     * @param number
     * @return byte[4]
     */
    public static byte[] int2Bytes (int number) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) ((number >> 24) & 0xFF);
        byteArray[1] = (byte) ((number >> 16) & 0xFF);
        byteArray[2] = (byte) ((number >> 8) & 0xFF);
        byteArray[3] = (byte) (number & 0xFF);
        return byteArray;
    }
    
    /**
     * Convert long to byte[8] array.
     * @param number
     * @return byte[8]
     */
    public static byte[] long2Bytes (long number) {
        byte[] byteArray = new byte[8];
        byteArray[0] = (byte) ((number >> 56) & 0xFF);
        byteArray[1] = (byte) ((number >> 48) & 0xFF);
        byteArray[2] = (byte) ((number >> 40) & 0xFF);
        byteArray[3] = (byte) ((number >> 32) & 0xFF);
        byteArray[4] = (byte) ((number >> 24) & 0xFF);
        byteArray[5] = (byte) ((number >> 16) & 0xFF);
        byteArray[6] = (byte) ((number >> 8) & 0xFF);
        byteArray[7] = (byte) (number & 0xFF);
        return byteArray;
    }
    
    /**
     * Convert byte[8] array to int.
     * @param B
     * @return int
     */
    /*
    public static long byteArrayToLong (byte[] B) {
        ByteBuffer Bb = ByteBuffer.allocate(B.length);
        Bb.put(B);
        return Bb.getLong();
    }*/
    public static long byteArrayToLong (byte[] B) {
        return ((B[0] & 0xFFL) << 56) |
         ((B[1] & 0xFFL) << 48) |
         ((B[2] & 0xFFL) << 40) |
         ((B[3] & 0xFFL) << 32) |
         ((B[4] & 0xFFL) << 24) |
         ((B[5] & 0xFFL) << 16) |
         ((B[6] & 0xFFL) <<  8) |
         ((B[7] & 0xFFL) <<  0) ;
    }
    
    /**
     * Convert byte[4] array to int.
     * @param B
     * @return int
     */
    public static int byteArrayToInt (byte[] B) { 
        return (B[0] << 24) + ((B[1] & 0xFF) << 16) + ((B[2] & 0xFF) << 8) + (B[3] & 0xFF); 
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
    
    /**
     * Pop next int from list.
     * @param Bytes
     * @return int
     */
    public static int nextInt (List<Byte> Bytes) {
        byte[] arrayInt = new byte[INT_LENGHT/BYTE_LENGHT];
        for (int i = 0; i < INT_LENGHT/BYTE_LENGHT; i++) {
            arrayInt[i] = Bytes.remove(0);
        }
        return byteArrayToInt(arrayInt);
    }
    
    /**
     * Pop next long from list.
     * @param Bytes
     * @return long
     */
    public static long nextLong (List<Byte> Bytes) {
        byte[] arrayLong = new byte[LONG_LENGHT/BYTE_LENGHT];
        for (int i = 0; i < LONG_LENGHT/BYTE_LENGHT; i++) {
            arrayLong[i] = Bytes.remove(0);
        }
        return byteArrayToLong(arrayLong);
    }
    
    /**
     * Pop next string of specified lenght.
     * @param Bytes
     * @param len
     * @return string[len]
     */
    public static String nextString (List<Byte> Bytes, int len) {
        byte[] arrayString = new byte[len];
        for (int i = 0; i < len; i++) {
            arrayString[i] = Bytes.remove(0);
        }
        return new String(arrayString);
    }
}
