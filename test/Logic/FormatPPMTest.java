package Logic;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class FormatPPMTest {
    
    public FormatPPMTest() {
    }

    @Test
    public void loadPicture() {
        File path = new File(System.getProperty("user.dir") + "/Data/");
        String name = "Face-smile";
        String format = ".ppm";
        File picturePath = new File(path, name + format);
        FormatPPM ppm = new FormatPPM();
        try {
            ppm.loadPicture(picturePath);
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
    }
    
    @Test
    public void readHeader() {
        File path = new File(System.getProperty("user.dir") + "/Data/");
        String name = "Face-smile";
        String format = ".ppm";
        File picturePath = new File(path, name + format);
        FormatPPM ppm = new FormatPPM();
        try {
            ppm.loadPicture(picturePath);
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
        //TODO
    }
    
}
