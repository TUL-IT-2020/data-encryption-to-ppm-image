package Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author pytel
 */
public class Picture {

    private File path;
    private String name;
    private String format;
    private int width;
    private int height;
    private ArrayList<ArrayList<Pixel>> data;

    public Picture(File picturePath) throws FileNotFoundException, IOException{
        if (!picturePath.exists()) throw new FileNotFoundException();
        this.path = picturePath;
        this.name = picturePath.getName().split("\\.")[0];
        this.format = "." + picturePath.getName().split("\\.")[1];
        
        PictureDataInterface pictureData = new FormatPPM();
        switch (this.format) {
            case ".ppm": pictureData.loadPicture(picturePath); break;
            default:
                throw new UnsupportedOperationException("Not supported yet format: " + this.format);
        }
        
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
