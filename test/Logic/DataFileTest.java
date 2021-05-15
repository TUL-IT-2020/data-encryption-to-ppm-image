package Logic;

import Logic.TestData.TestFileData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public void TestReadFile() {
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            try {
                df.ReadFile();
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
        }
    }

    @Test
    public void TestGetNBites() {
        for (TestFileData file : files) {
            DataFile df = loadFile(file.filePath);
            int chunk = 8;
            try {
                df.ReadFile();
                for (int i = 0; i < df.getFileSize() * 8 / chunk; i++) {
                    System.out.format("Chunk content: %s\n", (char) (df.getNextNbites(chunk) & 0xFF));
                }
                //assert df.getFileSize() == size : "Invalid size: " +
                //        df.getFileSize() + " != " + size;
            } catch (IOException ex) {
                System.out.format("ERROR: %s\n", ex);
            }
        }
    }

}
