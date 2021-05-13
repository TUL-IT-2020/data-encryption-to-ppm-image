package Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pytel
 */
public interface PictureDataInterface {
    
    public void loadPicture(File path) throws FileNotFoundException, IOException;
    
    public int getHeight();
    
    public int getwidth();
    
    public List<Pixel> getData();
    
    public boolean setData (ArrayList<Pixel> data);
    
    public void save2File(File path);
    
}
