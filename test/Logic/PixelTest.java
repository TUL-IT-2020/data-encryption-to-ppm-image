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
        System.out.format("Test pixel: %s\n", p);
    }
    
    @Test
    public void testSomeMethod() {
    }
    
}
