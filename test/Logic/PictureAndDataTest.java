package Logic;

import static Logic.PictureTest.pictures;
import Logic.TestData.TestFileData;
import Logic.TestData.TestPictureData;
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
    
    @Test
    public void willFileFitToPicture() {
        Picture p;
        DataFile df;
        int depthPerChannel = 1;
        for (TestPictureData picture : pictures) {
            p = loadPicture(picture.picturePath);
            p.setChunkSize(depthPerChannel);
            for (TestFileData file : files) {
                df = loadFile(file.filePath);
                /*
                System.out.format("File: %s %s fit to picture: %s.\n", 
                        file.name,
                        p.canStoreBytes() > df.getGrossSize()*8 ? "wil" : "wont",
                        picture.name
                        );*/
            }
        }
    }
    
    @Test
    public void numberOfStoredFilesInEmptyPicture() {
        int number;
        int chunk = 1;
        Picture p;
        p = loadPicture(pictures[1].picturePath);
        p.setChunkSize(chunk);
        number = p.getNumberOfStoredFiles();
        assertEquals(-1, number);
    }
    
    @Test
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
        System.out.format("Capacity: %d file size: %d\n", picture.canStoreBytes(), df.getGrossSize());
        assert picture.canStoreBytes() > df.getGrossSize() : 
                "File is to big! " + picture.canStoreBytes() + " < "+ df.getGrossSize();
        
        // store file
        assert (picture.addFile(df) ? 
                picture.canStoreBytes() > df.getGrossSize() : 
                picture.canStoreBytes() < df.getGrossSize()
                ) : "ERROR";
        
        // is file stored?
        int number = picture.getNumberOfStoredFiles();
        assertEquals("Number of stored files: " + number, 1, number);
        
        // load file content
        DataFile[] dtfs = picture.storedFiles();
        assert dtfs != null : "ERROR : did not store any files!";
        for (DataFile dtf : dtfs) {
            //System.out.format("%s\n", dtf);
        }
    }
    
    //@Test
    public void storeFileToPictureAndSaveIt() {
        int chunk = 8;
        Picture picture;
        DataFile df;
        picture = loadPicture(pictures[1].picturePath);
        
        // load data file
        for (TestFileData file : files) {
            df = loadFile(file.filePath);
            System.out.format("File name: %s\n", df.getName());
            
            picture.setChunkSize(chunk);
            picture.removeAllStored();
            
            // can I store the file?
            System.out.format("Free space: %d file size: %d\n", picture.freeSpace(), df.getGrossSize());
        
            if (picture.freeSpace() < df.getGrossSize()) continue;
            assert picture.freeSpace() > df.getGrossSize() : 
                "File is to big! " + picture.freeSpace() + " < "+ df.getGrossSize();
            
            // store file
            assert (picture.addFile(df)
                    ? picture.freeSpace() > df.getGrossSize()
                    : picture.freeSpace() < df.getGrossSize()) : "ERROR";
        
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
                    createNewFile(newDataFile);
                    dtf.save2File(newDataFile);
                    //newDataFile.delete();
                } catch (IOException ex) {
                    assert false : "ERROR: " + ex;
                }
            }
        }
        //newPictureFile.delete();
    }
    
    //@Test
    public void numberOfStoredFiles() {
        int chunk = 4;
        DataFile df;
        Picture picture = loadPicture(pictures[2].picturePath);
        picture.setChunkSize(chunk);
        System.out.format("Number of files to store: %d\n", files.length);
        int numberOfStored = 0;
        // load data file
        for (TestFileData file : files) {
            df = loadFile(file.filePath);

            // can I store the file?
            if (picture.canStoreBytes() < df.getGrossSize()) continue;
            
            assert picture.canStoreBytes() > df.getGrossSize() : 
                "File is to big! " + picture.canStoreBytes() + " < "+ df.getGrossSize();
            
            // store files
            
            if (picture.addFile(df)) numberOfStored++;
        }
        
        // how many files are stored?
        int number = picture.getNumberOfStoredFiles();
        assertEquals("Number of stored files: " + number, numberOfStored, number);
        
        // load file content
        DataFile[] dtfs = picture.storedFiles();
        assert dtfs != null : "ERROR : did not store any files!";
        assert dtfs.length == numberOfStored : "Invalid number of loaded files.";
        for (DataFile dtf : dtfs) {
            System.out.format("%s\n", dtf);
        }
    }
    
    //@Test
    public void storeFilesToPictureAndThenRemoveThem() {
        int chunk = 4;
        DataFile df;
        boolean fileStored;
        Picture picture = loadPicture(pictures[1].picturePath);
        picture.setChunkSize(chunk);
        System.out.format("Number of files to store: %d\n", files.length);
        // load data file
        for (TestFileData file : files) {
            df = loadFile(file.filePath);

            // can I store the file?
            if (picture.freeSpace() < df.getGrossSize()) continue;
            assert picture.freeSpace() > df.getGrossSize() : 
                "File is to big! " + picture.freeSpace() + " < "+ df.getGrossSize();

            // store files
            fileStored = picture.addFile(df);
            assert (fileStored
                    ? picture.freeSpace() > df.getGrossSize()
                    : picture.freeSpace() < df.getGrossSize()) : "ERROR";
            if (fileStored) System.out.format("File stored.\n");
        }
        System.out.format("Number of stored files: %d\n", picture.getNumberOfStoredFiles());
        picture.removeAllStored();
        
        // how many files are stored?
        int number = picture.getNumberOfStoredFiles();
        assertEquals("Number of stored files: " + number, 0, number);
    }
    
    @Test
    public void storeChosedFileToPictureAndSaveIt() {
        System.out.println("storeChosedFileToPictureAndSaveIt");
        int chunk = 8;
        Picture picture;
        Picture newPicture;
        DataFile df;
        picture = loadPicture(pictures[2].picturePath);
        
        // load data file
        df = loadFile(files[2].filePath);
        
        // store file
        picture.setChunkSize(chunk);
        System.out.format("Capacity: %d file size: %d\n", picture.canStoreBytes(), df.getGrossSize());
        if (!picture.addFile(df)) System.out.format("File was not stored!\n");
        if (picture.freeSpace() < df.getGrossSize()) return;
        
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
                createNewFile(newDataFile);
                dtf.save2File(newDataFile);
                //newDataFile.delete();
            } catch (IOException ex) {
                assert false : "ERROR: " + ex;
            }
        }
        
        //newPictureFile.delete();
        System.out.println();
    }
    
}
