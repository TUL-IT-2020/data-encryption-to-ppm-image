package Logic;

import Logic.PictureFormats.FormatPPM;
import Tools.Counter;
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
    private static final int NUMBER_OF_CHANELS = 3;
    
    // chain link value
    private static final int EMPTY = -1;
    private static final int LAST = 0;
    
    private static final boolean DEBUG = false;
    
    private File path;
    private String name;
    private String format;
    
    private PictureFormatInterface pictureDataAndInfo;
    
    private List<Pixel> data = new ArrayList();
    private Counter byteIndex;  // nth Byte
    private Counter indexOfPixel;
    private Counter indexOfChannel;
    private Counter indexOfbite = null;
    private int chunkSize;
    
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
        this.byteIndex = new Counter(data.size());
        indexOfPixel = new Counter(data.size() * NUMBER_OF_CHANELS);
        indexOfChannel = new Counter(NUMBER_OF_CHANELS);
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
        return chunkSize;
    }
    
    public void setChunkSize (int chunk) {
        if (chunk < 1 || chunkSize > 8) throw new IllegalArgumentException("Invalid chunk size!");
        this.chunkSize = chunk;
        this.indexOfbite = new Counter(chunk);
        //System.out.format("Counter size set to: %d.\n", this.chunkSize);
    }
    
    private void setByteIndex (int index) {
        byteIndex.setNumber(index);
    }
    
    private int getFirstLinkByteIndex () {
        return HEADER_KEY.length();
    }

    /**
     * Return how much bites can picture store (capacity - stored files).
     * @return 
     */
    public long canStorebites() {
        long capacity = 3*getwidth()*getHeight()*chunkSize;
        return capacity;
    }
    
    public boolean isPictureEmpty() {
        setByteIndex(getFirstLinkByteIndex());
        return loadNextInt() == EMPTY;
    }

    public void save2File(File newFile) throws IOException {
        // store data
        pictureDataAndInfo.setData(data);
        
        // save2File data
        pictureDataAndInfo.save2File(newFile);
    }
    
    private void calculateIndexes () {
        indexOfPixel.setNumber(byteIndex.getNumber()*BYTE_LENGHT / (NUMBER_OF_CHANELS*chunkSize));
        indexOfChannel.setNumber((byteIndex.getNumber()*BYTE_LENGHT % (NUMBER_OF_CHANELS*chunkSize)) / (chunkSize));
        indexOfbite.setNumber((byteIndex.getNumber()*BYTE_LENGHT) % (chunkSize));
        if (DEBUG) {
            System.out.format("Pixel: %d \t Chanel: %d \t bite: %d\n",
                    indexOfPixel.getNumber(), indexOfChannel.getNumber(), indexOfbite.getNumber());
        }
    }
    
    private byte loadNextByte () {
        int carry;
        int ret = 0;
        Pixel pixel;
        int channel = -1;
        int bit;
        int position;
        int nthBit;
        calculateIndexes();
        for (int i = 0; i < BYTE_LENGHT; i++) {
            // find next bit
            pixel = data.get(indexOfPixel.getNumber());
            switch (indexOfChannel.getNumber()) {
                case 0:
                    channel = pixel.getR();
                    break;
                case 1:
                    channel = pixel.getG();
                    break;
                case 2:
                    channel = pixel.getB();
                    break;
                default:
                    assert false : "Implementation error!";
            }
            bit = nthBitFromRight(channel, this.chunkSize - indexOfbite.getNumber() -1);
            position = BYTE_LENGHT-i-1;
            nthBit = bit << position;
            ret = ret | nthBit;
            
            //System.out.format("Bit index size: %d\t", indexOfbite.getSize());
            if (DEBUG) {
                System.out.format("old: %s\t", int2BinString(channel & 0xFF, 8));
                System.out.format("new: %s\t", int2BinString((ret | nthBit) & 0xFF, 8));
                System.out.format("%d -> %d, bit %d, chanel %d index.\n", bit, position, indexOfbite.getNumber(), indexOfChannel.getNumber());
            }
            
            // move index to next bit
            carry = indexOfbite.add();
            carry = indexOfChannel.add(carry);
            if (indexOfPixel.add(carry) != 0) throw new IndexOutOfBoundsException();
        }
        // next
        byteIndex.add();
        return (byte)ret;
    }
    
    private int loadNextInt () {
        return loadNextByte() << 24 | loadNextByte() << 16 | loadNextByte() << 8 | loadNextByte();
    }
    
    private byte[] loadNextNBytes (int length) {
        byte[] Bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            Bytes[i] = loadNextByte();
        }
        return Bytes;
    }
    
    private void storeNextByte (byte B) {
        if (DEBUG) System.out.format("Byte to store: %8.8s\n", Integer.toString(B & 0xFF,2));
        int carry;
        Pixel pixel;
        int channel = -1;
        int bit;
        int mask;
        int position;
        int nthBit;
        calculateIndexes();
        for (int i = 0; i < BYTE_LENGHT; i++) {
            // find next bit
            pixel = data.get(indexOfPixel.getNumber());
            switch (indexOfChannel.getNumber()) {
                case 0:
                    channel = pixel.getR();
                    break;
                case 1:
                    channel = pixel.getG();
                    break;
                case 2:
                    channel = pixel.getB();
                    break;
                default:
                    assert false : "Implementation error!";
            }
            // bit to store
            bit = nthBitFromLeft(B, i);
            // shifted to right position
            position = this.chunkSize - indexOfbite.getNumber() -1;
            mask = 1 << position;
            nthBit = bit << position;
            // write bite to pixel chanel
            /*
            if (DEBUG) {
            System.out.format("old: %8.8s\t", Integer.toString(channel & 0xFF,2));
            System.out.format("Masked: %8.8s\t", Integer.toString(channel & ~mask & 0xFF,2));
            System.out.format("new: %8.8s\t", Integer.toString(((channel & ~mask) | nthBit) & 0xFF,2));
            System.out.format("%d -> %d shift to %d index, chanel %d\n", bit, position, indexOfbite.getNumber(), indexOfChannel.getNumber());
            }*/
            channel = (channel & ~mask) | nthBit;
            switch (indexOfChannel.getNumber()) {
                case 0:
                    pixel.setR((byte) channel);
                    break;
                case 1:
                    pixel.setG((byte) channel);
                    break;
                case 2:
                    pixel.setB((byte) channel);
                    break;
                default:
                    assert false : "Implementation error!";
            }
            // store modified pixel
            data.set(indexOfPixel.getNumber(), pixel);
            
            // move index to next bit
            carry = indexOfbite.add();
            carry = indexOfChannel.add(carry);
            if (indexOfPixel.add(carry) != 0) throw new IndexOutOfBoundsException();
        }
        // next
        byteIndex.add();
    }
    
    private void storeNextNBytes (byte[] Bytes) {
        for (int i = 0; i < Bytes.length; i++) {
            storeNextByte(Bytes[i]);
        }
    }

    private boolean checkForHeader () {
        setByteIndex(0);
        StringBuilder pictureContent = new StringBuilder();
        char nextChar;
        for (int i = 0; i < HEADER_KEY.length(); i++) {
            nextChar = (char)loadNextByte();
            //nextChar = byte2char(loadNextByte());
            pictureContent.append(nextChar);
        }
        if (DEBUG) System.out.format("Founded: %s Key: %s\n", pictureContent.toString(), HEADER_KEY);
        return HEADER_KEY.compareTo(pictureContent.toString()) == 0;
    }
    
    private boolean createAndStoreHeader () {
        // TODO check if key fit to file !!!
        setByteIndex(0);
        byte[] array = HEADER_KEY.getBytes();
        for (byte B : array) {
            storeNextByte(B);
            //System.out.format(" %d \n", (int)B);
        }
        // empty chain termination
        storeNextNBytes(int2Bytes(EMPTY));
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
        setByteIndex(linkIndex);
        // store first link index
        links.add(linkIndex);
        while ((linkIndex = loadNextInt()) != LAST) {
            setByteIndex(linkIndex);
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
        setByteIndex(ByteIndex);
        if (DEBUG) System.out.format(" --- Chain header ---\n");
        int nextLink = loadNextInt();
        // Int Header lenght
        int headLenght = loadNextInt();
        // Int Data lenght
        int dataLenght = loadNextInt();
        
        if (DEBUG) System.out.format(" --- Load file header ---\n");
        int alreadyLoaded = 2*INT_LENGHT/BYTE_LENGHT;
        byte[] head = loadNextNBytes(headLenght - alreadyLoaded);
        if (DEBUG) System.out.format(" --- Load data ---\n");
        byte[] boady = loadNextNBytes(dataLenght);
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
        setByteIndex(ByteIndex);
        int nextLink = loadNextInt();
        // Int Header lenght
        int headLenght = loadNextInt();
        // Int Data lenght
        int dataLenght = loadNextInt();
        // calculate size
        int size = headLenght + dataLenght + INT_LENGHT/BYTE_LENGHT;
        if (DEBUG) System.out.format("Offset: %d + head len: %d Data len: %d\n", headLenght, dataLenght, INT_LENGHT/BYTE_LENGHT);
        // update link to new chain
        setByteIndex(ByteIndex);
        int newLinkIndex = ByteIndex + size;
        storeNextNBytes(int2Bytes(newLinkIndex));
        return newLinkIndex;
    }
    
    public boolean addFile(DataFile df) {
        if (df.getGrossSize() > canStorebites()) return false; // File is to big
        byte B;
        // check for file header
        if (!checkForHeader()) {    // nema zatim hlavicku
            createAndStoreHeader();
        }
        
        // set link index
        int LinkByteIndex = addNextLink();
        setByteIndex(LinkByteIndex);
        if (DEBUG) System.out.format("Link Byte index: %d\n", LinkByteIndex);
        
        // chain termination
        storeNextNBytes(int2Bytes(LAST));
        
        // Header
        if (DEBUG) System.out.format(" --- Store header ---\n");
        for (int i = 0; i < df.getHeaderSize(); i++) {
            B = df.getHeadByte(i);
            storeNextByte(B);
        }
        // Data
        if (DEBUG) System.out.format(" --- Store Data ----\n");
        for (int i = 0; i < df.getDataSize(); i++) {
            B = df.getDataByte(i);
            storeNextByte(B);
        }
        if (DEBUG) System.out.format(" --- Storing done ---\n");
        return true;
    }
    
    public void removeAllStored() {
        setByteIndex(getFirstLinkByteIndex());
        storeNextNBytes(int2Bytes(EMPTY));
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
        p.setByteIndex(0);
        p.setChunkSize(8);
        bytes.add(p.loadNextByte());
        bytes.add(p.loadNextByte());
        bytes.add(p.loadNextByte());
        bytes.add(p.loadNextByte());
        bytes.add(p.loadNextByte());
        bytes.add(p.loadNextByte());
        bytes.add(p.loadNextByte());
        bytes.add(p.loadNextByte());
        bytes.add(p.loadNextByte());
        for (int i = 0; i < bytes.size(); i++) {
            System.out.format("%s", byte2char(bytes.get(i)));
        }
        System.out.println();
    }

}
