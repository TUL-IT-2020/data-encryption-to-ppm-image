package Logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author pytel
 */
public class FormatPPM implements PictureDataInterface{

    private String MagicNumber;
    private int width;
    private int height;
    private ArrayList<Pixel> data;
    
    @Override
    public void loadPicture(File path) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            if (!readHead(br)) throw new IOException("Unsoported type of ppm format");
            //if (!readData(br)) throw new IOException("Ivalid data format");
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
        //if (Integer.parseInt(br.readLine()) != 255) return false;
        
        return true;
    }
    
    private boolean readData (BufferedReader br) throws IOException {
        switch (MagicNumber) {
            case "P3":
                for (int i = 0; i < this.height*this.width; i++) {
                    System.out.format("%d ", i);
                    data.add(readPixel(br));
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
    public ArrayList<Pixel> getData() {
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
