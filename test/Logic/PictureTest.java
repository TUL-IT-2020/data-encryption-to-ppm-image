package Logic;

import Logic.TestData.TestPictureData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class PictureTest {
    
    public static TestPictureData[] pictures = TestData.pictures;
    
    static File path = new File(System.getProperty("user.dir") + "/Data/");
    static String name = "Face-smile";
    static String format = ".ppm";
    
    public PictureTest() {
    }
    
    public Picture loadPicture (File picturePath) {
        Picture p = null;
        try {
            p = new Picture(picturePath);
        } catch (FileNotFoundException e) {
            System.out.format("ERROR: %s\n", e);
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
        return p;
    }
    
    @Test
    public void createPicture() {
        File picturePath = new File(path, name + format);
        Picture p = null;
        for (TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
        }
        assertNotNull(p);
    }
    
    @Test
    public void createPicture_InvalidFile() {
        String name = "notAPicture";
        File picturePath = new File(path, name + format);
        Picture p = loadPicture(picturePath);
        assert p == null : "Konstruktor does not work.";
    }
    
    @Test
    public void createPicture_ValidFile() {
        File picturePath = new File(path, name + format);
        Picture p = null;
        try {
            //System.out.format("%s\n", picturePath.getAbsolutePath());
            p = new Picture(picturePath);
        } catch (FileNotFoundException e) {
            System.out.format("ERROR: %s\n", e);
            assert false : "Konstruktor does not work.";
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
    }
    
    @Test
    public void createPictureAndGetName() {
        File picturePath = new File(path, name + format);
        Picture p = loadPicture(picturePath);
        assertNotNull(p);
        assert p.getName().compareTo(name) == 0 : "Invalid name: " + p.getName() + " != " + name;
    }
    
    
    @Test
    public void createPictureAndGetFormat() {
        File picturePath = new File(path, name + format);
        Picture p = loadPicture(picturePath);
        assertNotNull(p);
        assert p.getFormat().compareTo(format) == 0 : "Invalid name: " + p.getFormat() + " != " + format;
        
    }

    /**
     * Test of main method, of class Picture.
     */
    @Test
    public void testMain() {
    }
    
}
