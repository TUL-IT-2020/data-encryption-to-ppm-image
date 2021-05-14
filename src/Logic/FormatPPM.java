package Logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pytel
 */
public class FormatPPM implements PictureDataInterface{

    private String MagicNumber;
    private int width;
    private int height;
    private List<Pixel> data = new ArrayList();
    
    @Override
    public void loadPicture(File path) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            if (!readHead(br)) throw new IOException("Unsoported type of ppm format");
            if (!readData(br)) throw new IOException("Ivalid data format");
        }
    }
    
    private boolean readHead (BufferedReader br) throws IOException {
        String line = null;
        // Format
        this.MagicNumber = br.readLine();
        switch (MagicNumber) {
            case "P3": break;
            default:
                return false;   // unsuported format
        }
        //System.out.format("Magic number: %s\n", MagicNumber);
        // Gimp heading
        line = br.readLine(); 
        if (line.contains("#")) line = br.readLine();
        // dimensions
        String[] array = line.split(" ");
        //System.out.format("Array len: %d\n", array.length);
        if (array.length != 2) return false;
        this.width = Integer.parseInt(array[0]);
        this.height = Integer.parseInt(array[1]);
        // Maxval
        if (Integer.parseInt(br.readLine()) != 255) return false;
        
        return true;
    }
    
    private boolean readData (BufferedReader br) throws IOException {
        data.clear();
        switch (MagicNumber) {
            case "P3":
                Pixel pixel;
                for (int i = 0; i < this.height*this.width; i++) {
                    //System.out.format("pixel index: %d \n", i);
                    if ((pixel = readPixel(br)) == null) return false;
                    //System.out.format("pixel: %s \n", pixel);
                    data.add(pixel);
                }
                break;
            default:
                return false;   // unsuported format
        }
        return true;
    }
    
    private Pixel readPixel (BufferedReader br) throws IOException{
        int R = Integer.parseInt(br.readLine());
        int G = Integer.parseInt(br.readLine());
        int B = Integer.parseInt(br.readLine());
        //System.out.format("pixel data: R:%d G:%d B:%d\n", R, G, B);
        return new Pixel((byte)R, (byte)G, (byte)B);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getwidth() {
        return width;
    }

    @Override
    public List<Pixel> getData() {
        return data;
    }

    @Override
    public boolean setData(ArrayList<Pixel> data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void save2File(File path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
