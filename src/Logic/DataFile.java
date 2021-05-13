package Logic;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class stroting information to store/load from picture.
 * @author pytel
 */
public class DataFile {

    private File path;
    private String name;
    private String format;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public void loadFile(File filePath) throws FileNotFoundException {
        if (!filePath.exists()) throw new FileNotFoundException();
        this.path = filePath;
        this.name = filePath.getName().split("\\.")[0];
        this.format = "." + filePath.getName().split("\\.")[1];
    }

    public String getName() {
        return name;
    }
    
}
