package Logic.PictureFormats;

import Logic.Pixel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Interface for reading and storing pictures.
 * @author pytel
 */
public interface PictureFormatInterface {
    
    /**
     * Load picture from path.
     * @param path
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void loadPicture(File path) throws IOException;
    
    /**
     * @return picture height
     */
    public int getHeight();
    
    /**
     * @return picture width
     */
    public int getwidth();
    
    /**
     * @return Pixel data as 1D list of Pixels
     */
    public List<Pixel> getData();
    
    /**
     * Sotore data to picture.
     * @param data
     * @return true if data could be stored
     */
    public boolean setData (List<Pixel> data);
    
    /**
     * Save picture to path.
     * @param path
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void save2File(File path) throws IOException;
    
}
