package Logic;

import Logic.PictureFormats.FormatPPM;
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
    
    @Test
    public void loadPicture() {
        System.out.format("Create test: \t");
        FormatPPM ppm;
        for (TestPictureData picture : pictures) {
            ppm = new FormatPPM();
            try {
                ppm.loadPicture(picture.picturePath);
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
            assertNotNull(ppm);
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void readHeader() {
        System.out.format("Read header: \t");
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
        System.out.format("Done\n");
    }
    
    @Test
    public void readData() {
        System.out.format("Read data: \t");
        FormatPPM ppm = new FormatPPM();
        for (TestPictureData picture : pictures) {
            try {
                ppm.loadPicture(picture.picturePath);
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
            assertNotNull(ppm.getData());
            //System.out.format("Data: %s\n", ppm.getData());
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void readAndStoreValidData() {
        System.out.format("Read and store back data: \t");
        FormatPPM ppm;
        List<Pixel> data;
        for (TestPictureData picture : pictures) {
            ppm = new FormatPPM();
            try {
                ppm.loadPicture(picture.picturePath);
                data = ppm.getData();
                assertTrue(ppm.setData(data));
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
            assertNotNull(ppm.getData());
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void storeInvalidData() {
        System.out.format("Store invalid data: \t");
        FormatPPM ppm = new FormatPPM();
        List<Pixel> data = new ArrayList();
        try {
            ppm.loadPicture(pictures[0].picturePath);
            assertFalse(ppm.setData(data));
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
        System.out.format("Done\n");
    }
    
    private boolean isSamePixel (Pixel p1, Pixel p2) {
        if (p1.getR() != p2.getR()) return false;
        if (p1.getG() != p2.getG()) return false;
        if (p1.getB() != p2.getB()) return false;
        return true;
    }
    
    private boolean containSamePixelData (List<Pixel> data1, List<Pixel> data2) {
        for (int i = 0; i < data1.size(); i++) {
            if (!isSamePixel(data1.get(i), data2.get(i))) return false;
        }
        return true;
    }
    
    @Test
    public void savePicture() {
        System.out.format("Save picture: \t");
        FormatPPM pictureOld = new FormatPPM();
        FormatPPM pictureNew = new FormatPPM();
        File newFile;
        String newName;
        List<Pixel> dataOld;
        List<Pixel> dataNew;
        for (TestPictureData picture : pictures) {
            try {
                pictureOld.loadPicture(picture.picturePath);
                dataOld = pictureOld.getData();
                // create new file
                newName = picture.name + "_generated" + picture.format;
                newFile = new File(picture.path, newName);
                if (newFile.exists()) {
                    // file already exist, removing file.
                    newFile.delete();
                }
                newFile.createNewFile();
                pictureOld.save2File(newFile);
                // load stored file back
                pictureNew.loadPicture(newFile);
                dataNew = pictureNew.getData();
                // copmare files
                Assert.assertEquals(pictureOld.getHeight(), pictureNew.getHeight());
                Assert.assertEquals(pictureOld.getwidth(), pictureNew.getwidth());
                Assert.assertEquals(dataOld.size(), dataNew.size());
                assertTrue(containSamePixelData(dataOld, dataNew));
                // remove genrated file
                newFile.delete();
            } catch (IOException ex) {
                assert false : "ERROR: " + ex + "\n";
            }
        }
        System.out.format("Done\n");
    }
    
}
