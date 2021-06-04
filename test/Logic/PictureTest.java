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
    
    public Picture loadPicture (File picturePath) {
        Picture p = null;
        try {
            p = new Picture(picturePath);
        } catch (FileNotFoundException e) {
            //System.out.format("ERROR: %s\n", e);
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
        return p;
    }
    
    
    @Test
    public void createPicture() {
        System.out.format("Create test: \t");
        Picture p;
        for (TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
            assertNotNull(p);
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void createPicture_InvalidFile() {
        System.out.format("Invalid file: \t");
        String name = "notAPicture";
        String format = ".ppm";
        File picturePath = new File(pictures[0].path, name + format);
        Picture p = loadPicture(picturePath);
        assert p == null : "Konstruktor does not work.";
        System.out.format("Done\n");
    }
    
    @Test
    public void createPicture_ValidFile() {
        System.out.format("Create pictures: \t");
        Picture p;
        for (TestPictureData picture : pictures) {
            try {
                //System.out.format("%s\n", picturePath.getAbsolutePath());
                p = new Picture(picture.picturePath);
                assertNotNull(p);
            } catch (FileNotFoundException e) {
                System.out.format("ERROR: %s\n", e);
                assert false : "Konstruktor does not work.";
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void createPictureAndGetName() {
        System.out.format("Pictures name: \t");
        Picture p;
        for (TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
            assertNotNull(p);
            //System.out.format("Picture name: %s\n", picture.name);
            assert p.getName().compareTo(picture.name) == 0 : "Invalid name: " + 
                    p.getName() + " != " + picture.name;
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void createPictureAndGetFormat() {
        System.out.format("Pictures format: \t");
        Picture p;
        for (TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
            assertNotNull(p);
            assert p.getFormat().compareTo(picture.format) == 0 : "Invalid format: " + 
                    p.getFormat() + " != " + picture.format;
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void createPictureAndGetSize() {
        System.out.format("Pictures get size: \t");
        Picture p;
        for (TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
            assertNotNull(p);
            assert p.getHeight() == picture.height: "Invalid height: " + 
                    p.getHeight() + " != " + picture.height;
            assert p.getwidth() == picture.width: "Invalid width: " + 
                    p.getwidth() + " != " + picture.width;
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void PictureCanStoreData() {
        System.out.format("Pictures can store data: \t");
        Picture p;
        int maxDepthPerChannel = 8;
        long calculatedCapacity;
        for (TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
            for (int depthPerChannel = 1; depthPerChannel < maxDepthPerChannel; depthPerChannel++) {
                p.setChunkSize(depthPerChannel);
                calculatedCapacity = 3 * picture.width * picture.height * depthPerChannel / 8;
                assert p.canStoreBytes() == calculatedCapacity : "Invalid size to store: "
                        + p.canStoreBytes() + " != " + calculatedCapacity;
            }
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void savePicture() {
        System.out.format("Save picture: \t");
        Picture p;
        for (TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
            String newName = p.getName() + "_generated" + p.getFormat();
            File newFile = new File(picture.path, newName);
            //System.out.format("New name: %s\n", newName);
            try {
                newFile.createNewFile();
                p.save2File(newFile);
                newFile.delete();
            } catch (IOException ex) {
                assert false : "ERROR: " + ex + "\n";
            }
        }
        System.out.format("Done\n");
    }
    
}
