package Logic;

import Logic.PictureFormats.FormatPPM;
import static Tools.ByteTools.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Logic.PictureFormats.PictureFormatInterface;

/**
 *
 * @author pytel
 */
public class Picture {

    private static final String HEADER_KEY = "42";    // TODO chenge it ASCII !!!
    public static final int NUMBER_OF_CHANELS = 3;
    
    // chain link value
    private static final int EMPTY = -1;
    private static final int LAST = 0;
    
    private static final boolean DEBUG = false;
    
    private File path;
    private String name;
    private String format;
    
    private PictureFormatInterface pictureDataAndInfo;
    
    private RandomAccessPixelStream data;
    
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
        
        this.data = new RandomAccessPixelStream(pictureDataAndInfo.getData());
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

    public int getChunkSize() {
        return data.getChunkSize();
    }
    
    public void setChunkSize (int chunk) {
        if (chunk < 1 || chunk > 8) throw new IllegalArgumentException("Invalid chunk size!");
        this.data.setChunkSize(chunk);
        //System.out.format("Counter size set to: %d.\n", this.chunkSize);
    }
    
    
    private int getFirstLinkByteIndex () {
        return HEADER_KEY.length();
    }

    /**
     * Return how much Bytes can picture store.
     * @return 
     */
    public long canStoreBytes () {
        long capacity = 3*getwidth()*getHeight()*data.getChunkSize()/8;
        return capacity;
    }
    
    /**
     * Return how much more Bytes can picture store (capacity - stored files).
     * @return 
     */
    public long freeSpace () {
        int last = -1;
        int ByteIndex = findStoredFile(last);
        if (ByteIndex == -1) { // zadny soubor jeste nebyl zapsan
            return canStoreBytes() - getFirstLinkByteIndex();
        }
        data.setByteIndex(ByteIndex);
        int nextLink = data.loadNextInt();
        // Int Header lenght
        int headLenght = data.loadNextInt();
        // Int Data lenght
        int dataLenght = data.loadNextInt();
        // calculate size
        int size = headLenght + dataLenght + INT_LENGHT/BYTE_LENGHT;
        // update link to new chain
        int newLinkIndex = ByteIndex + size;
        return canStoreBytes() - newLinkIndex;
    }
    
    public boolean isPictureEmpty() {
        data.setByteIndex(getFirstLinkByteIndex());
        return data.loadNextInt() == EMPTY;
    }

    public void save2File(File newFile) throws IOException {
        // store data
        pictureDataAndInfo.setData(data);
        
        // save2File data
        pictureDataAndInfo.save2File(newFile);
    }
    

    private boolean checkForHeader () {
        data.setByteIndex(0);
        StringBuilder pictureContent = new StringBuilder();
        char nextChar;
        for (int i = 0; i < HEADER_KEY.length(); i++) {
            nextChar = (char)data.loadNextByte();
            //nextChar = byte2char(loadNextByte());
            pictureContent.append(nextChar);
        }
        if (DEBUG) System.out.format("Founded: %s Key: %s\n", pictureContent.toString(), HEADER_KEY);
        return HEADER_KEY.compareTo(pictureContent.toString()) == 0;
    }
    
    private boolean createAndStoreHeader () {
        // TODO check if key fit to file !!!
        data.setByteIndex(0);
        byte[] array = HEADER_KEY.getBytes();
        for (byte B : array) {
            data.storeNextByte(B);
            //System.out.format(" %d \n", (int)B);
        }
        // empty chain termination
        data.storeNextNBytes(int2Bytes(EMPTY));
        //System.out.format(" %d %d \n", data.get(0).getR(), data.get(0).getG());
        /*
        for (int i = 0; i < HEADER_KEY.length()/NUMBER_OF_CHANELS; i++) {
            System.out.format(" %s \n", data.get(i));
        }*/
        return true;
    }
    
    private int[] getStoredFileLinks () {
        if (!checkForHeader()) {
            return null;
        }
        int linkIndex;
        List<Integer> links = new ArrayList();
        if (isPictureEmpty()) {
            return new int[0];
        }
        // set ByteIndexToFirstLink
        linkIndex = getFirstLinkByteIndex();
        data.setByteIndex(linkIndex);
        // store first link index
        links.add(linkIndex);
        while ((linkIndex = data.loadNextInt()) != LAST) {
            data.setByteIndex(linkIndex);
            links.add(linkIndex);
        }
        return links.stream().mapToInt(i->i).toArray();
    }
    
    /**
     * If file do not exist return -1.
     * If picture contain none files return -1;
     * If asked for invalid index return -1;
     * @param index of nth file
     * @return ByteIndex of nth file.
     */
    private int findStoredFile (int index) {
        int[] links = getStoredFileLinks ();
        if (links == null) return -1;
        if (index >= links.length) return -1;
        if (links.length == 0) return -1; // empty
        if (index == -1) index = links.length-1;
        return links[index];
    }
    
    public int getNumberOfStoredFiles () {
        if (!checkForHeader()) return -1;
        if (DEBUG) System.out.format("Header exist.\n");
        return getStoredFileLinks().length;
    }
    
    private DataFile loadFile(int ByteIndex) {
        data.setByteIndex(ByteIndex);
        if (DEBUG) System.out.format(" --- Chain header ---\n");
        int nextLink = data.loadNextInt();
        // Int Header lenght
        int headLenght = data.loadNextInt();
        // Int Data lenght
        int dataLenght = data.loadNextInt();
        
        if (DEBUG) System.out.format(" --- Load file header ---\n");
        int alreadyLoaded = 2*INT_LENGHT/BYTE_LENGHT;
        byte[] head = data.loadNextNBytes(headLenght - alreadyLoaded);
        if (DEBUG) System.out.format(" --- Load data ---\n");
        System.out.format(" Data len: %d\n", dataLenght);
        byte[] boady = data.loadNextNBytes(dataLenght);
        if (DEBUG) System.out.format(" --- Loading done ---\n");
        DataFile dtf = new DataFile(head, boady);
        return dtf;
    }

    public DataFile[] storedFiles() {
        DataFile[] dtfs = null;
        if (!checkForHeader()) {
            return dtfs;
        }
        int[] links = getStoredFileLinks();
        dtfs = new DataFile[links.length];
        for (int i = 0; i < links.length; i++) {
            dtfs[i] = loadFile(links[i]);
        }
        return dtfs;
    }
    
    /**
     * 
     * @return index of file link to chain
     */
    private int addNextLink () {
        int last = -1;
        int ByteIndex = findStoredFile(last);
        if (ByteIndex == -1) { // zadny soubor jeste nebyl zapsan
            return getFirstLinkByteIndex();
        }
        data.setByteIndex(ByteIndex);
        int nextLink = data.loadNextInt();
        // Int Header lenght
        int headLenght = data.loadNextInt();
        // Int Data lenght
        int dataLenght = data.loadNextInt();
        // calculate size
        int size = headLenght + dataLenght + INT_LENGHT/BYTE_LENGHT;
        if (DEBUG) System.out.format("Offset: %d + head len: %d Data len: %d\n", headLenght, dataLenght, INT_LENGHT/BYTE_LENGHT);
        // update link to new chain
        data.setByteIndex(ByteIndex);
        int newLinkIndex = ByteIndex + size;
        data.storeNextNBytes(int2Bytes(newLinkIndex));
        return newLinkIndex;
    }
    
    public boolean addFile(DataFile df) {
        if (df.getGrossSize() > freeSpace()) return false; // File is to big
        byte B;
        // check for file header
        if (!checkForHeader()) {    // nema zatim hlavicku
            createAndStoreHeader();
        }
        
        // set link index
        int LinkByteIndex = addNextLink();
        data.setByteIndex(LinkByteIndex);
        if (DEBUG) System.out.format("Link Byte index: %d\n", LinkByteIndex);
        
        // chain termination
        data.storeNextNBytes(int2Bytes(LAST));
        
        // Header
        if (DEBUG) System.out.format(" --- Store header ---\n");
        for (int i = 0; i < df.getHeaderSize(); i++) {
            B = df.getHeadByte(i);
            data.storeNextByte(B);
        }
        // Data
        if (DEBUG) System.out.format(" --- Store Data ----\n");
        for (int i = 0; i < df.getDataSize(); i++) {
            B = df.getDataByte(i);
            data.storeNextByte(B);
        }
        if (DEBUG) System.out.format(" --- Storing done ---\n");
        return true;
    }
    
    public void removeAllStored() {
        data.setByteIndex(getFirstLinkByteIndex());
        data.storeNextNBytes(int2Bytes(EMPTY));
    }
    
    /**
     * Private metode test helper.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File path = new File(System.getProperty("user.dir") + "/Data/testDataSet");
        List<Byte> bytes = new ArrayList();
        File picturePath = new File(path, "white.ppm"); //"white.ppm", "small.ppm"
        Picture p = null;
        try {
            p = new Picture(picturePath);
        } catch (IOException ex) {
            assert false : "Iner ERROR";
        }
        p.data.setByteIndex(0);
        p.setChunkSize(8);
        bytes.add(p.data.loadNextByte());
        bytes.add(p.data.loadNextByte());
        bytes.add(p.data.loadNextByte());
        bytes.add(p.data.loadNextByte());
        bytes.add(p.data.loadNextByte());
        bytes.add(p.data.loadNextByte());
        bytes.add(p.data.loadNextByte());
        bytes.add(p.data.loadNextByte());
        bytes.add(p.data.loadNextByte());
        for (int i = 0; i < bytes.size(); i++) {
            System.out.format("%s", byte2char(bytes.get(i)));
        }
        System.out.println();
    }

}
