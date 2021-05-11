package Logic;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author pytel
 */
public class Picture {

    private File path;
    private String name;
    private String format;

    public Picture(File picturePath) throws FileNotFoundException{
        if (!picturePath.exists()) throw new FileNotFoundException();
        this.path = picturePath;
        this.name = picturePath.getName().split("\\.")[0];
        this.format = "." + picturePath.getName().split("\\.")[1];
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }
    
}
