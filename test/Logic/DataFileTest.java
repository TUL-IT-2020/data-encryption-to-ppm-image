package Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class DataFileTest {

    public static File path = new File(System.getProperty("user.dir") + "/Data/testDataSet");
    public static String name = "test";
    public static String format = ".txt";
    public static File filePath = new File(path, name + format);
    public static long size = 6;

    public DataFileTest() {
    }

    public DataFile loadFile(File filePath) {
        DataFile df = new DataFile();
        try {
            df.loadFile(filePath);
        } catch (FileNotFoundException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
        return df;
    }

    @Test
    public void createDataFile() {
        DataFile df = loadFile(filePath);
        assertNotNull(df);
    }

    @Test
    public void createDataFileAndGetName() {
        DataFile df = loadFile(filePath);
        assert df.getName().compareTo(name) == 0 : "Invalid name: "
                + df.getName() + " != " + name;
    }

    @Test
    public void GetFileSize() {
        DataFile df = loadFile(filePath);
        //System.out.format("File Size: %d\n", df.getFileSize());
        assert df.getFileSize() == size : "Invalid size: "
                + df.getFileSize() + " != " + size;
    }

    @Test
    public void TestReadFile() {
        DataFile df = loadFile(filePath);
        try {
            df.ReadFile();
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
    }

    @Test
    public void TestGetNBites() {
        DataFile df = loadFile(filePath);
        int chunk = 8;
        try {
            df.ReadFile();
            for (int i = 0; i < df.getFileSize()*8/chunk; i++) {
                System.out.format("Chunk content: %s\n", (char)(df.getNextNbites(chunk) & 0xFF));
            }
            //assert df.getFileSize() == size : "Invalid size: " +
            //        df.getFileSize() + " != " + size;
        } catch (IOException ex) {
            System.out.format("ERROR: %s\n", ex);
        }
    }

}
