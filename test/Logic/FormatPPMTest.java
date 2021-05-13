package Logic;

import java.io.File;
import java.io.IOException;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class FormatPPMTest {
    
    class TestPictureData {
        public File path;
        public String name;
        public String format;
        public int height;
        public int width;
        File picturePath;

        public TestPictureData(File path, String name, String format, int height, int width) {
            this.path = path;
            this.name = name;
            this.format = format;
            this.height = height;
            this.width = width;
            picturePath = new File(path, name + format);
        }
    }
    
    public TestPictureData picture = new TestPictureData(
            new File(System.getProperty("user.dir") + "/Data/"),
            "Face-smile", ".ppm", 50, 50);
    
    public FormatPPMTest() {
    }

    @Test
    public void loadPicture() {
        FormatPPM ppm = new FormatPPM();
        try {
            ppm.loadPicture(picture.picturePath);
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
    }
    
    @Test
    public void readHeader() {
        FormatPPM ppm = new FormatPPM();
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
    
    @Test
    public void readData() {
        FormatPPM ppm = new FormatPPM();
        try {
            ppm.loadPicture(picture.picturePath);
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
        Assert.assertNotNull(ppm.getData());
        System.out.format("Data: %s\n", ppm.getData());
    }
    
}
