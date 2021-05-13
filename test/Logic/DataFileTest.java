package Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class DataFileTest {
    
    public static File path = new File(System.getProperty("user.dir") + "/Data/");
    public static String name = "test";
    public static String format = ".txt";
    public static File filePath = new File(path, name + format);
    
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
        assert df.getName().compareTo(name) == 0 : "Invalid name: " + df.getName() + " != " + name;
    }
    
}
