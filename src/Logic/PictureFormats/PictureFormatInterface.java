package Logic.PictureFormats;

import Logic.Pixel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 
 * @author pytel
 */
public interface PictureFormatInterface {
    
    public void loadPicture(File path) throws FileNotFoundException, IOException;
    
    public int getHeight();
    
    public int getwidth();
    
    public List<Pixel> getData();
    
    public boolean setData (List<Pixel> data);
    
    public void save2File(File path) throws FileNotFoundException, IOException;
    
}
