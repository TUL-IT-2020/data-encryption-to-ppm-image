package Logic;

import java.util.ArrayList;
import java.util.List;
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
        int maxHeight = 100;
        for (int width = 1; width < maxWidth; width++) {
            for (int height = 1; height < maxHeight; height++) {
                int depthPerChannel = 8;
                List<Pixel> data = generatePictureData(width, height, (byte) 255, (byte) 255, (byte) 255);
                RandomAccessPixelStream raf = new RandomAccessPixelStream(data);
                raf.setChunkSize(depthPerChannel);
                int calculatedCapacity = 3 * width * height * depthPerChannel / 8;
                assert raf.getCapacity() == calculatedCapacity : "Invalid size to store: "
                        + raf.getCapacity() + " != " + calculatedCapacity;
            }
        }
        System.out.format("Done\n");
    }
    
}
