package Logic;

import static Logic.Picture.NUMBER_OF_CHANELS;
import static Tools.ByteTools.*;
import Tools.Counter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pytel
 */
public class RandomAccessPixelStream {
    
    private static final boolean DEBUG = false;
    
    private List<Pixel> data = new ArrayList();
    private int chunkSize;
    private Counter byteIndex;  // nth Byte
    private Counter indexOfPixel;
    private Counter indexOfChannel;
    private Counter indexOfbite = null;
    
    public RandomAccessPixelStream (List<Pixel> data) {
        
        indexOfPixel = new Counter(data.size());
        indexOfChannel = new Counter(NUMBER_OF_CHANELS);
    }
    
    public void setChunkSize (int chunk) {
        this.chunkSize = chunk;
        this.indexOfbite = new Counter(chunk);
        this.byteIndex = new Counter((int)canStoreBytes());
        //System.out.format("Counter size set to: %d.\n", this.chunkSize);
    }
    
    public void setByteIndex (int index) {
        byteIndex.setNumber(index);
    }
    
    public int getChunkSize() {
        return chunkSize;
    }
    
    public List<Pixel> getDataContent () {
        return data;
    }
    
    private void calculateIndexes () {
        indexOfPixel.setNumber(byteIndex.getNumber()*BYTE_LENGHT / (NUMBER_OF_CHANELS*chunkSize));
        indexOfChannel.setNumber((byteIndex.getNumber()*BYTE_LENGHT % (NUMBER_OF_CHANELS*chunkSize)) / (chunkSize));
        indexOfbite.setNumber((byteIndex.getNumber()*BYTE_LENGHT) % (chunkSize));
        if (DEBUG) {
            System.out.format("Pixel: %d \t Chanel: %d \t bite: %d\n",
                    indexOfPixel.getNumber(), indexOfChannel.getNumber(), indexOfbite.getNumber());
        }
    }
    
    public byte loadNextByte () {
        int carry;
        int ret = 0;
        Pixel pixel;
        int channel = -1;
        int bit;
        int position;
        int nthBit;
        calculateIndexes();
        for (int i = 0; i < BYTE_LENGHT; i++) {
            // find next bit
            pixel = data.get(indexOfPixel.getNumber());
            switch (indexOfChannel.getNumber()) {
                case 0:
                    channel = pixel.getR();
                    break;
                case 1:
                    channel = pixel.getG();
                    break;
                case 2:
                    channel = pixel.getB();
                    break;
                default:
                    assert false : "Implementation error!";
            }
            bit = nthBitFromRight(channel, this.chunkSize - indexOfbite.getNumber() -1);
            position = BYTE_LENGHT-i-1;
            nthBit = bit << position;
            ret = ret | nthBit;
            
            //System.out.format("Bit index size: %d\t", indexOfbite.getSize());
            if (DEBUG) {
                System.out.format("old: %s\t", int2BinString(channel & 0xFF, 8));
                System.out.format("new: %s\t", int2BinString((ret | nthBit) & 0xFF, 8));
                System.out.format("%d -> %d, bit %d, chanel %d index.\n", bit, position, indexOfbite.getNumber(), indexOfChannel.getNumber());
            }
            
            // move index to next bit
            carry = indexOfbite.add();
            carry = indexOfChannel.add(carry);
            if (indexOfPixel.add(carry) != 0) throw new IndexOutOfBoundsException();
        }
        // next
        byteIndex.add();
        return (byte)ret;
    }
    
    public int loadNextInt () {
        return loadNextByte() << 24 | loadNextByte() << 16 | loadNextByte() << 8 | loadNextByte();
    }
    
    public byte[] loadNextNBytes (int length) {
        byte[] Bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            Bytes[i] = loadNextByte();
        }
        return Bytes;
    }
    
    public void storeNextByte (byte B) {
        if (DEBUG) System.out.format("Byte to store: %8.8s\n", Integer.toString(B & 0xFF,2));
        int carry;
        Pixel pixel;
        int channel = -1;
        int bit;
        int mask;
        int position;
        int nthBit;
        calculateIndexes();
        for (int i = 0; i < BYTE_LENGHT; i++) {
            // find next bit
            pixel = data.get(indexOfPixel.getNumber());
            switch (indexOfChannel.getNumber()) {
                case 0:
                    channel = pixel.getR();
                    break;
                case 1:
                    channel = pixel.getG();
                    break;
                case 2:
                    channel = pixel.getB();
                    break;
                default:
                    assert false : "Implementation error!";
            }
            // bit to store
            bit = nthBitFromLeft(B, i);
            // shifted to right position
            position = this.chunkSize - indexOfbite.getNumber() -1;
            mask = 1 << position;
            nthBit = bit << position;
            // write bite to pixel chanel
            /*
            if (DEBUG) {
            System.out.format("old: %8.8s\t", Integer.toString(channel & 0xFF,2));
            System.out.format("Masked: %8.8s\t", Integer.toString(channel & ~mask & 0xFF,2));
            System.out.format("new: %8.8s\t", Integer.toString(((channel & ~mask) | nthBit) & 0xFF,2));
            System.out.format("%d -> %d shift to %d index, chanel %d\n", bit, position, indexOfbite.getNumber(), indexOfChannel.getNumber());
            }*/
            channel = (channel & ~mask) | nthBit;
            switch (indexOfChannel.getNumber()) {
                case 0:
                    pixel.setR((byte) channel);
                    break;
                case 1:
                    pixel.setG((byte) channel);
                    break;
                case 2:
                    pixel.setB((byte) channel);
                    break;
                default:
                    assert false : "Implementation error!";
            }
            // store modified pixel
            data.set(indexOfPixel.getNumber(), pixel);
            
            // move index to next bit
            carry = indexOfbite.add();
            carry = indexOfChannel.add(carry);
            if (indexOfPixel.add(carry) != 0) throw new IndexOutOfBoundsException();
        }
        // next
        byteIndex.add();
    }
    
    public void storeNextNBytes (byte[] Bytes) {
        for (int i = 0; i < Bytes.length; i++) {
            storeNextByte(Bytes[i]);
        }
    }
}
