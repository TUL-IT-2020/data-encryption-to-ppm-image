package Logic;

import Logic.TestData.TestPictureData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        assertNotNull(ppm);
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
        for (TestPictureData picture : pictures) {
            try {
                ppm.loadPicture(picture.picturePath);
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
            assertNotNull(ppm.getData());
            System.out.format("Data: %s\n", ppm.getData());
        }
    }
    
    @Test
    public void readAndStoreValidData() {
        FormatPPM ppm = new FormatPPM();
        List<Pixel> data;
        for (TestPictureData picture : pictures) {
            try {
                ppm.loadPicture(picture.picturePath);
                data = ppm.getData();
                assertTrue(ppm.setData(data));
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
            assertNotNull(ppm.getData());
        }
    }
    
    @Test
    public void storeInvalidData() {
        FormatPPM ppm = new FormatPPM();
        List<Pixel> data = new ArrayList();
        try {
            ppm.loadPicture(pictures[0].picturePath);
            assertFalse(ppm.setData(data));
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
    }
    
    @Test
    public void savePicture() {
        FormatPPM ppm = new FormatPPM();
        File newFile;
        String newName;
        for (TestPictureData picture : pictures) {
            try {
                ppm.loadPicture(picture.picturePath);
                // create new file
                newName = picture.name + "_generated" + picture.format;
                newFile = new File(picture.path, newName);
                if (newFile.exists()) {
                    // file already exist, removing file.
                    newFile.delete();
                }
                newFile.createNewFile();
                ppm.save2File(newFile);
                // remove genrated file
                newFile.delete();
            } catch (IOException ex) {
                assert false : "ERROR: " + ex + "\n";
            }
        }
    }
    
}
