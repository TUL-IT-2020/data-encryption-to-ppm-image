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
public class Picture {

    private static String HEADER_KEY = "42";    // TODO chenge it
    
    private File path;
    private String name;
    private String format;
    private List<Pixel> data = new ArrayList();
    
    private PictureDataInterface pictureDataAndInfo;

    public Picture(File picturePath) throws FileNotFoundException, IOException{
        if (!picturePath.exists()) throw new FileNotFoundException();
        this.path = picturePath;
        this.name = picturePath.getName().split("\\.")[0];
        this.format = "." + picturePath.getName().split("\\.")[1];
        
        pictureDataAndInfo = new FormatPPM();
        switch (this.format) {
            case ".ppm": pictureDataAndInfo.loadPicture(picturePath); break;
            default:
                throw new UnsupportedOperationException("Not supported yet format: " + this.format);
        }
        
        this.data = pictureDataAndInfo.getData();
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public int getHeight() {
        return pictureDataAndInfo.getHeight();
    }

    public int getwidth() {
        return pictureDataAndInfo.getwidth();
    }

    /**
     * Return how much bites can picture store
     * @param depthPerChannel
     * @return 
     */
    public long canStorebites(int depthPerChannel) {
        if (depthPerChannel< 0 || depthPerChannel > 8) return -1;
        long capacity = 3*getwidth()*getHeight()*depthPerChannel;
        return capacity;
    }
    
    private boolean createNewFile (File file) throws IOException {
        if (file.exists()) {
            // file already exist, removing file.
            file.delete();
        }
        return file.createNewFile();
    }

    public void save(String newName) throws IOException{
        // create file
        File newFile = new File(path.getParentFile(), newName);
        //System.out.format("New file: %s\n", newFile.getAbsoluteFile());
        createNewFile(newFile);
        
        // store data
        pictureDataAndInfo.setData(data);
        
        // save data
        pictureDataAndInfo.save2File(newFile);
    }
    
    
    private int byteIndex;  // nth Byte
    private int chunk;
    private void setChunk (int chunk) {
        this.chunk = chunk;
    }
    
    private void setByteIndex (int index) {
        byteIndex = index;
    }
    
    private byte nextByte () {
        int indexOfPixel = byteIndex*8 / (3*chunk);
        int indexOfChanel = byteIndex*8 % (3*chunk);
        // TODO
        byteIndex++;
        return (byte)0;
    }
    
    private int nextInt () {
        return nextByte() << 8 | nextByte();
    }

    private boolean checkForHeader(int chunk) {
        setChunk(chunk);
        StringBuilder pictureContent = new StringBuilder();
        char nextChar;
        for (int i = 0; i < HEADER_KEY.length(); i++) {
            nextChar = (char)(nextByte() & 0xFF);
            pictureContent.append(nextChar);
        }
        return HEADER_KEY.compareTo(pictureContent.toString()) == 0;
    }
    
    public int getNumberOfStoredFiles (int chunk) {
        if (!checkForHeader(chunk)) return -1;
        // TODO
        return -1;
    }

    /*
    public DataFile[] storedFiles() {
        DataFile[] dtfs = null;
        if (!checkHeader()) {
            return dtfs;
        }
    }*/
    
    /*
    public boolean addFile(DataFile df) {
        // File is to big
    }*/
}
