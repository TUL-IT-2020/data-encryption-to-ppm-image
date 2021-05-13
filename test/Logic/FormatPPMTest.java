package Logic;

import Logic.TestData.TestPictureData;
import java.io.IOException;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class FormatPPMTest {
    
    public static TestPictureData[] pictures = TestData.pictures;
    
    public FormatPPMTest() {
    }

    @Test
    public void loadPicture() {
        FormatPPM ppm = new FormatPPM();
        for (TestPictureData picture : pictures) {
            try {
                ppm.loadPicture(picture.picturePath);
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
        }
    }
    
    @Test
    public void readHeader() {
        FormatPPM ppm = new FormatPPM();
        for (TestPictureData picture : pictures) {
            try {
                ppm.loadPicture(picture.picturePath);
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
            assertEquals(ppm.getHeight(), picture.height);
            assertEquals(ppm.getwidth(), picture.width);
            //System.out.format("Width: %s\n", ppm.getwidth());
            //System.out.format("Height: %s\n", ppm.getHeight());
        }
    }
    
    @Test
    public void readData() {
        FormatPPM ppm = new FormatPPM();
        //for (TestPictureData picture : pictures) {
            try {
                ppm.loadPicture(pictures[0].picturePath);
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
            Assert.assertNotNull(ppm.getData());
            System.out.format("Data: %s\n", ppm.getData());
        //}
    }
    
}
