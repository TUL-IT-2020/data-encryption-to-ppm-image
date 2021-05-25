package Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class RandomAccessPixelStreamTest {
    
    public RandomAccessPixelStreamTest() {
    }

    public static List<Pixel> generatePictureData (int height, int width, byte R, byte G, byte B) {
        List<Pixel> data = new ArrayList();
        for (int i = 0; i < height*width; i++) {
            data.add(new Pixel(R,G,B));
        }
        return data;
    }
    
    @Test
    public void createRAF() {
        System.out.format("CreateTest: \t");
        List<Pixel> data = generatePictureData(10, 10, (byte)255, (byte)255, (byte)255);
        RandomAccessPixelStream raf = new RandomAccessPixelStream(data);
        assertNotNull(raf);
        System.out.format("Done\n");
    }
    
    @Test
    public void capacityTest() {
        System.out.format("CapacityTest: \t");
        int maxWidth = 100;
        int maxHeight = 10;
        int maxDepthPerChannel = 8;
        for (int width = 1; width < maxWidth; width++) {
            for (int height = 1; height < maxHeight; height++) {
                for (int depthPerChannel = 1; depthPerChannel < maxDepthPerChannel; depthPerChannel++) {
                    List<Pixel> data = generatePictureData(width, height, (byte) 255, (byte) 255, (byte) 255);
                    RandomAccessPixelStream raf = new RandomAccessPixelStream(data);
                    raf.setChunkSize(depthPerChannel);
                    int calculatedCapacity = 3 * width * height * depthPerChannel / 8;
                    assert raf.getCapacity() == calculatedCapacity : "Invalid size to store: "
                            + raf.getCapacity() + " != " + calculatedCapacity;
                }
            }
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void writeAndRead() {
        System.out.format("Write and read:\t");
        Random rand = new Random();
        int width = 100;
        int height = 1000;
        int maxDepthPerChannel = 8;
        for (int depthPerChannel = 1; depthPerChannel < maxDepthPerChannel; depthPerChannel++) {
            List<Pixel> pixels = generatePictureData(width, height, (byte) 255, (byte) 255, (byte) 255);
            RandomAccessPixelStream raf = new RandomAccessPixelStream(pixels);
            raf.setChunkSize(depthPerChannel);

            // data to write
            int size = (int) raf.getCapacity() - 1;
            //System.out.format("\nCapacity : %d\n", size);
            byte[] dataIn = new byte[size];
            for (int i = 0; i < size; i++) {
                dataIn[i] = (byte) rand.nextInt(255);
            }

            // write
            raf.setByteIndex(0);
            for (byte B : dataIn) {
                raf.storeNextByte(B);
            }

            // read
            raf.setByteIndex(0);
            for (int i = 0; i < size; i++) {
                assert dataIn[i] == raf.loadNextByte() : "ERROR: index:" + i;
            }
        }
        
        System.out.format("Done\n");
    }
}
