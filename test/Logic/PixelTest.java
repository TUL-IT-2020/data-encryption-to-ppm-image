package Logic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class PixelTest {
    
    public PixelTest() {
    }
    
    @Test
    public void newPixel () {
        Pixel p = new Pixel((byte)255,(byte)255,(byte)255);
    }
    
    @Test
    public void printPixel () {
        Pixel p = new Pixel((byte)255,(byte)255,(byte)255);
        System.out.format("Test pixel: %s\n", p);
    }
    
    @Test
    public void getValue () {
        int[][] RGB = {
            {0, 0, 0},
            {127, 127, 127},
            {255, 255, 255}
        };
        for (int[] rgb : RGB) {
            Pixel p = new Pixel((byte)rgb[0],(byte)rgb[1],(byte)rgb[2]);
            assertEquals(rgb[0], p.getR());
            assertEquals(rgb[1], p.getG());
            assertEquals(rgb[2], p.getB());
        }
    }
    
    @Test
    public void setValue () {
        Pixel p = new Pixel((byte)0,(byte)0,(byte)0);
        int[][] RGB = {
            {1, 2, 3},
            {127, 127, 127},
            {255, 255, 255}
        };
        for (int[] rgb : RGB) {
            p.setR((byte)rgb[0]);
            p.setG((byte)rgb[1]);
            p.setB((byte)rgb[2]);
            assertEquals(rgb[0], p.getR());
            assertEquals(rgb[1], p.getG());
            assertEquals(rgb[2], p.getB());
        }
    }
    
    @Test
    public void testSomeMethod() {
    }
    
}
