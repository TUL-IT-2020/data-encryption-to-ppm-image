package Logic;

import Logic.TestData.TestFileData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            assertNotNull(df);
        }
    }

    @Test
    public void createDataFileAndGetName() {
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            assert df.getName().compareTo(file.name) == 0 : "Invalid name: "
                    + df.getName() + " != " + file.name;
        }
    }

    @Test
    public void GetFileSize() {
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            //System.out.format("File Size: %d\n", df.getFileSize());
            assert df.getFileSize() == file.size : "Invalid size: "
                    + df.getFileSize() + " != " + file.size;
        }
    }

    @Test
    public void TestGetNBites() {
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            int chunk = 8;
            for (int i = 0; i < df.getFileSize() * 8 / chunk; i++) {
                // TODO test it
                //System.out.format("Chunk content: %s\n", (char) (df.getNextNbites(chunk) & 0xFF));
            }
        }
    }
    
    @Test
    public void TestGetDataBytes() {
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            for (int i = 0; i < df.getFileSize(); i++) {
                //System.out.format("Byte content: %s\n", (char) (df.getDataByte(i) & 0xFF));
            }
        }
    }
    
    @Test
    public void TestGetHeadBytes() {
        byte B;
        DataFile df;
        DataFile ndf;
        byte[] header;
        for (TestFileData file : files) {
            df = loadFile(file.filePath);
            // read header Bytes
            for (int i = 0; i < df.getHeaderSize(); i++) {
                B = df.getHeadByte(i);
                System.out.format("Byte content: %s\n", (char) (B & 0xFF));
            }
            // create file from Bytes header
            int startIndex = 2*4;
            header = new byte[(int)df.getHeaderSize()-startIndex];
            for (int i = startIndex; i < df.getHeaderSize(); i++) {
                header[i-startIndex] = df.getHeadByte(i);
            }
            ndf = new DataFile(header);
            System.out.format("%s\n", ndf);
        }
    }

}
