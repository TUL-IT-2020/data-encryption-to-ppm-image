package Logic.PictureFormats;

import Logic.Pixel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implemetntatoin of PictureFormatInterface for use with PPM format.
 * Compatible types: P3
 * @author pytel
 */
public class FormatPPM implements PictureFormatInterface {
    
    private static final int MAX_VALUE = 255;
    private String magicNumber;
    private int width;
    private int height;
    private List<Pixel> data = new ArrayList();
    
    @Override
    public void loadPicture(File path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            if (!readHead(br)) throw new IOException("Unsoported type of ppm format");
            if (!readData(br)) throw new IOException("Ivalid data format");
        }
    }
    
    /**
     * Read header data from picture (br).
     * @param br
     * @return true if picture have valid format
     * @throws IOException 
     */
    private boolean readHead (BufferedReader br) throws IOException {
        String line = null;
        // Format
        this.magicNumber = br.readLine();
        switch (magicNumber) {
            case "P3": break;
            default:
                return false;   // unsuported format
        }
        //System.out.format("Magic number: %s\n", MagicNumber);
        // comment
        do {
            line = br.readLine();
        } while (line.contains("#"));
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
    
    /**
     * Read pixel data from picture (br).
     * @param br
     * @return true if data could be read.
     * @throws IOException 
     */
    private boolean readData (BufferedReader br) throws IOException {
        data.clear();
        switch (magicNumber) {
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
    
    /**
     * Read one pixel (R,G,B) from picture (br).
     * @param br
     * @return
     * @throws IOException 
     */
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
    public boolean setData (List<Pixel> data) {
        if (data.size() != width*height) return false;
        this.data = data;
        return true;
    }
    
    @Override
    public void save2File (File path) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            writeHead(bw);
            if (!writeData(bw)) throw new IOException("Ivalid data format");
        }
    }
    
    /**
     * Write picture header data back to disk (fw).
     * @param fw
     * @throws IOException 
     */
    private void writeHead (BufferedWriter bw) throws IOException {
        // Format
        bw.write(magicNumber + "\n");
        // heading
        bw.write("# Modified by my app.\n");  // TODO konstant
        // dimensions
        bw.write(this.width + " " + this.height + "\n");
        // Maxval
        bw.write(MAX_VALUE + "\n");
    }
    
    /**
     * Write picture pixel data back to disk (fw).
     * @param fw
     * @return false if invalid data format.
     * @throws IOException 
     */
    private boolean writeData (BufferedWriter bw) throws IOException {
        switch (magicNumber) {
            case "P3":
                for (Pixel pixel : data) {
                    if (!writeP3TypePixel(bw, pixel)) return false;
                }
                break;
            default:
                return false;   // unsuported format
        }
        return true;
    }
    
    /**
     * Check if the chanel is in valid range.
     * @param chanel
     * @return true if chanel value is in valid range
     */
    private boolean chanelValidRange (int chanel) {
        return chanel <= MAX_VALUE && chanel >= 0;
    }
    
    /**picture
     * Write pixel data back to disk (fw).
     * @param fw
     * @param pixel
     * @return false if pixel contain invalid data.
     * @throws IOException 
     */
    private boolean writeP3TypePixel (BufferedWriter bw, Pixel pixel) throws IOException{
        StringBuilder str = new StringBuilder();
        boolean validRange;
        int R = pixel.getR();
        int G = pixel.getG();
        int B = pixel.getB();
        validRange = chanelValidRange(R) && chanelValidRange(G) && chanelValidRange(B);
        if (!validRange) return false;
        str.append(String.valueOf(R)).append("\n");
        str.append(String.valueOf(G)).append("\n");
        str.append(String.valueOf(B)).append("\n");
        bw.write(str.toString());
        return true;
    }
}
