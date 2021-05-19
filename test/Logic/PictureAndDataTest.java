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
    
    private boolean createNewFile (File file) throws IOException {
        if (file.exists()) {
            // file already exist, removing file.
            file.delete();
        }
        return file.createNewFile();
    }
    
    //@Test
    public void willFileFitToPicture() {
        Picture p;
        DataFile df;
        int depthPerChannel = 1;
        for (TestData.TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
            p.setChunkSize(depthPerChannel);
            for (TestData.TestFileData file : files) {
                df = loadFile(file.filePath);
                /*
                System.out.format("File: %s %s fit to picture: %s.\n", 
                        file.name,
                        p.canStorebites() > df.getGrossSize()*8 ? "wil" : "wont",
                        picture.name
                        );*/
            }
        }
    }
    
    //@Test
    public void numberOfStoredFilesInEmptyPicture() {
        int number;
        int chunk = 1;
        Picture p;
        p = loadPicture(pictures[1].picturePath);
        p.setChunkSize(chunk);
        number = p.getNumberOfStoredFiles();
        assertEquals(-1, number);
    }
    
    //@Test
    public void listStoredFilesInEpmtyPicture() {
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
    
    //@Test
    public void storeFileToPicture() {
        int chunk = 4;
        DataFile df;
        Picture picture = loadPicture(pictures[1].picturePath);
        picture.setChunkSize(chunk);
        // load data file
        df = loadFile(files[0].filePath);
        
        // can I store the file?
        assert picture.canStorebites() > df.getGrossSize() : 
                "File is to big! " + picture.canStorebites() + " < "+ df.getGrossSize();
        
        // store file
        assert (picture.addFile(df) ? 
                picture.canStorebites() > df.getGrossSize() : 
                picture.canStorebites() < df.getGrossSize()
                ) : "ERROR";
        
        // is file stored?
        int number = picture.getNumberOfStoredFiles();
        assertEquals("Number of stored files: " + number, 1, number);
        
        // load file content
        DataFile[] dtfs = picture.storedFiles();
        assert dtfs != null : "ERROR : did not store any files!";
        for (DataFile dtf : dtfs) {
            System.out.format("%s\n", dtf);
        }
    }
    
    @Test
    public void storeFileToPictureAndSaveIt() {
        int chunk = 8;
        Picture picture;
        Picture newPicture;
        DataFile df;
        picture = loadPicture(pictures[1].picturePath);
        
        // load data file
        df = loadFile(files[0].filePath);
        
        // store file
        picture.setChunkSize(chunk);
        assert (picture.addFile(df) ? 
                picture.canStorebites() > df.getGrossSize() : 
                picture.canStorebites() < df.getGrossSize()
                ) : "ERROR";
        
        // save2File file to disk
        String newPictureName = picture.getName() + "_generated" + picture.getFormat();
        File newPictureFile = new File(pictures[1].path, newPictureName);
        try {
            createNewFile(newPictureFile);
            picture.save2File(newPictureFile);
        } catch (IOException ex) {
            assert false : "ERROR: " + ex;
        }
        
        // is file stored?
        DataFile[] dtfs = picture.storedFiles();
        assert dtfs != null : "ERROR : did not store any files!";
        
        // save files
        String newFileName;
        File newDataFile;
        for (DataFile dtf : dtfs) {
            System.out.format("%s\n", dtf);
            // save file to disk
            newFileName = dtf.getName() + "_from_picture" + dtf.getFormat();
            newDataFile = new File(pictures[1].path, newFileName);
            try {
                createNewFile(newPictureFile);
                dtf.save2File(newDataFile);
                //newDataFile.delete();
            } catch (IOException ex) {
                assert false : "ERROR: " + ex;
            }
        }
        
        // TODO
        //newPictureFile.delete();
    }
    
    // TODO
    // p.removeAllStored()
}
