package Logic;

import Tools.DataFileComparatorByName;
import Tools.DataFileComparatorByDate;
import Tools.DataFileComparatorBySize;
import static Logic.TestData.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class DataFileTest {
    
    public static TestFileData[] files = TestData.files;

    public DataFileTest() {
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
    public void createDataFile() {
        System.out.format("CreateTest: \t");
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            assertNotNull(df);
        }
        System.out.format("Done\n");
    }

    @Test
    public void createDataFileAndGetName() {
        System.out.format("Get name: \t");
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            assert df.getName().compareTo(file.name) == 0 : "Invalid name: "
                    + df.getName() + " != " + file.name;
        }
        System.out.format("Done\n");
    }

    @Test
    public void testGetFormat() {
        System.out.format("Get format: \t");
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            assert df.getFormat().compareTo(file.format) == 0 : "Invalid name: "
                    + df.getFormat() + " != " + file.format;
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void testGetDate() {
        System.out.format("Get date: \t");
        Date date;
        long dateInMils;
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            dateInMils = file.filePath.lastModified();
            date = new Date(dateInMils);
            //System.out.format("Last modified: %s\n", date);
            assert df.getLastModified() == dateInMils : "Invalid name: "
                    + df.getFormat() + " != " + file.format;
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void GetFileSize() {
        System.out.format("Get file size: \t");
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            //System.out.format("File Size: %d\n", df.getDataSize());
            assert df.getDataSize() == file.size : "Invalid size: "
                    + df.getDataSize() + " != " + file.size;
        }
        System.out.format("Done\n");
    }

    @Test
    public void TestGetNBites() {
        System.out.format("Get n bytes: \t");
        int chunk = 8;
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            for (int i = 0; i < df.getDataSize() * 8 / chunk; i++) {
                // TODO test it
                //System.out.format("Chunk content: %s\n", (char) (df.getNextNbites(chunk) & 0xFF));
            }
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void TestGetDataBytes() {
        System.out.format("Get data: \t");
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            for (int i = 0; i < df.getDataSize(); i++) {
                //System.out.format("Byte content: %s\n", (char) (df.getDataByte(i) & 0xFF));
            }
        }
        System.out.format("Done\n");
    }
    
    private void printDtfs (DataFile[] dtfs) {
        for (DataFile dtf : dtfs) {
            System.out.format("%s\n", dtf);
        }
    }
    
    private DataFile[] loadDtfs () {
        DataFile[] dtfs = new DataFile[files.length];
        for (int i = 0; i < files.length; i++) {
            dtfs[i] = loadFile(files[i].filePath);
        }
        return dtfs;
    }
    
    @Test
    public void sort() {
        System.out.format("Sort by name: \t");
        DataFile[] dtfs = loadDtfs ();
        Arrays.sort(dtfs);
        //printDtfs(dtfs);
        System.out.format("Done\n");
    }
    
    @Test
    public void sortByName() {
        System.out.format("Sort by name: \t");
        DataFile[] dtfs = loadDtfs ();
        Comparator compare = new DataFileComparatorByName();
        Arrays.sort(dtfs, compare);
        printDtfs(dtfs);
        System.out.format("Done\n");
    }
    
    @Test
    public void sortBySize() {
        System.out.format("Sort by size: \t");
        DataFile[] dtfs = loadDtfs ();
        Comparator compare = new DataFileComparatorBySize();
        Arrays.sort(dtfs, compare);
        printDtfs(dtfs);
        System.out.format("Done\n");
    }
    
    @Test
    public void sortByDate() {
        System.out.format("Sort by date: \t");
        DataFile[] dtfs = loadDtfs ();
        Comparator compare = new DataFileComparatorByDate();
        Arrays.sort(dtfs, compare);
        printDtfs(dtfs);
        System.out.format("Done\n");
    }
    
    @Test
    public void TestGetHeadBytes() {
        System.out.format("Get header bytes: \t");
        byte B;
        DataFile df;
        DataFile ndf;
        byte[] header;
        byte[] data;
        for (TestFileData file : files) {
            df = loadFile(file.filePath);
            // read header Bytes
            for (int i = 0; i < df.getHeaderSize(); i++) {
                B = df.getHeadByte(i);
                //System.out.format("Byte content: %s\n", (char) (B & 0xFF));
            }
            // create file from Bytes header
            int startIndex = 2*4;
            header = new byte[(int)df.getHeaderSize()-startIndex];
            data = new byte[(int)df.getDataSize()];
            for (int i = startIndex; i < df.getHeaderSize(); i++) {
                header[i-startIndex] = df.getHeadByte(i);
            }
            for (int i = 0; i < df.getDataSize(); i++) {
                data[i] = df.getDataByte(i);
            }
            ndf = new DataFile(header, data);
            //System.out.format("%s\n", ndf);
            Assert.assertEquals(df.getName(), ndf.getName());
            Assert.assertEquals(df.getFormat(), ndf.getFormat());
            Assert.assertEquals(df.getHeaderSize(), ndf.getHeaderSize());
            Assert.assertEquals(df.getDataSize(), ndf.getDataSize());
            Assert.assertEquals(df.getGrossSize(), ndf.getGrossSize());
            Assert.assertEquals(df.getLastModified(), ndf.getLastModified());
        }
        System.out.format("Done\n");
    }

}
