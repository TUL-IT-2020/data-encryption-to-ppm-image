
package Logic;

import static Logic.PictureTest.pictures;
import static Logic.TestData.files;
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
public class PictureAndDataTest {
    
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
    
    public DataFile loadFile(File filePath) {
        DataFile df = null;
        try {
            df = new DataFile(filePath);
        } catch (FileNotFoundException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
        return df;
    }
    
    @Test
    public void willFileFitToPicture() {
        Picture p;
        DataFile df;
        int depthPerChannel = 1;
        for (TestData.TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
            for (TestData.TestFileData file : files) {
                df = loadFile(file.filePath);
                System.out.format("File: %s %s fit to picture: %s.\n", 
                        file.name,
                        p.canStorebites(depthPerChannel) > df.getFileSize()*8 ? "wil" : "wont",
                        picture.name
                        );
            }
        }
    }
    
    
    @Test
    public void numberOfStoredFilesInPicture() {
        int number;
        int chunk = 1;
        Picture p;
        p = loadPicture(pictures[1].picturePath);
        number = p.getNumberOfStoredFiles(chunk);
        assertEquals(number, -1);
    }
    
    /*
    @Test
    public void listStoredFilesInPicture() {
        Picture p;
        DataFile[] dtfs;
        p = loadPicture(pictures[1].picturePath);
        dtfs = p.storedFiles();
        for (DataFile dtf : dtfs) {
            System.out.format("%s\n", dtf);
        }
    }*/
    
    @Test
    public void storeFileToPicture() {
        Picture p;
        DataFile df;
        p = loadPicture(pictures[1].picturePath);
        df = loadFile(files[0].filePath);
        
        //p.addFile(df);
        // TODO
        // p.removeAllStored()
        // 
        // 
    }
}
