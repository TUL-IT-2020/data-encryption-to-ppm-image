package Logic.PictureFormats;

import Logic.Pixel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Logic.PictureFormats.PictureFormatInterface;

/**
 *
 * @author pytel
 */
public class FormatPPM implements PictureFormatInterface {
    
    private static final int MAX_VALUE = 255;
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
        if (Integer.parseInt(br.readLine()) != MAX_VALUE) return false;
        
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
        // TODO poskytovat kopii !!!
        return data;
    }

    @Override
    public boolean setData(List<Pixel> data) {
        if (data.size() != width*height) return false;
        this.data = data;
        return true;
    }
    
    @Override
    public void save2File(File path) throws FileNotFoundException, IOException {
        try (FileWriter fw = new FileWriter(path)) {
            writeHead(fw);
            if (!writeData(fw)) throw new IOException("Ivalid data format");
        }
    }
    
    private void writeHead (FileWriter fw) throws IOException {
        // Format
        fw.write(MagicNumber + "\n");
        // heading
        fw.write("# Modified by my app.\n");  // TODO konstant
        // dimensions
        fw.write(this.width + " " + this.height + "\n");
        // Maxval
        fw.write(MAX_VALUE + "\n");
    }
    
    private boolean writeData (FileWriter fw) throws IOException {
        switch (MagicNumber) {
            case "P3":
                for (Pixel pixel : data) {
                    if (!writeP3TypePixel(fw, pixel)) return false;
                }
                break;
            default:
                return false;   // unsuported format
        }
        return true;
    }
    
    private boolean writeP3TypePixel (FileWriter fw, Pixel pixel) throws IOException{
        // TODO use string builder !!!
        int R = pixel.getR();
        int G = pixel.getG();
        int B = pixel.getB();
        fw.write(R + "\n");
        fw.write(G + "\n");
        fw.write(B + "\n");
        return (R <= MAX_VALUE && R >= 0 && G <= MAX_VALUE && G >= 0 && B <= MAX_VALUE && B >= 0 );
    }
}
