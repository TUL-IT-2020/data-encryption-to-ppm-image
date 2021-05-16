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
            p.setChunkSize(depthPerChannel);
            for (TestData.TestFileData file : files) {
                df = loadFile(file.filePath);
                System.out.format("File: %s %s fit to picture: %s.\n", 
                        file.name,
                        p.canStorebites() > df.getGrossSize()*8 ? "wil" : "wont",
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
        p.setChunkSize(chunk);
        number = p.getNumberOfStoredFiles();
        assertEquals(number, -1);
    }
    
    @Test
    public void listStoredFilesInPicture() {
        int chunk = 1;
        Picture p;
        DataFile[] dtfs;
        p = loadPicture(pictures[1].picturePath);
        p.setChunkSize(chunk);
        dtfs = p.storedFiles();
        if (dtfs != null) {
            for (DataFile dtf : dtfs) {
                System.out.format("%s\n", dtf);
            }
        }
    }
    
    @Test
    public void storeFileToPicture() {
        Picture picture;
        Picture newPicture;
        DataFile df;
        picture = loadPicture(pictures[1].picturePath);
        String newName = picture.getName() + "_generated" + picture.getFormat();
        File newFile = new File(pictures[1].path, newName);
        //System.out.format("New name: %s\n", newName);
        try {
            picture.save(newName);
        } catch (IOException ex) {
            assert false : "ERROR: " + ex + "\n";
        }
        newPicture = loadPicture(newFile);
        df = loadFile(files[0].filePath);
        assert (newPicture.addFile(df) ? 
                newPicture.canStorebites() > df.getFileSize() : 
                newPicture.canStorebites() < df.getFileSize()
                ) : "ERROR";
        // TODO
        // p.removeAllStored()
        // 
        // 
        newFile.delete();
    }
}
