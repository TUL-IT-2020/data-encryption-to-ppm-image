package Logic;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * @author pytel
 */
public interface PictureDataInterface {
    
    public void loadPicture(File path);
    
    public int getHeight();
    
    public int getwidth();
    
    public ArrayList<Pixel> getData();
    
    public boolean setData (ArrayList<Pixel> data);
    
    public void save2File(File path);
    
}
