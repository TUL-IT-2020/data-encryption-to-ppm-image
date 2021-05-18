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
import Tools.ByteTools;

/**
 *
 * @author pytel
 */
public class Picture {

    private static final String HEADER_KEY = "42";    // TODO chenge it ASCII !!!
    private static final int NUMBER_OF_CHANELS = 3;
    private static final int BYTE_LEN = ByteTools.BYTE_LENGHT;
    
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
        if (chunk < 1) throw new IllegalArgumentException("Invalid chunk size!");
        this.chunkSize = chunk;
        this.indexOfbite = new Counter(chunkSize);
    }
    
    private void setByteIndex (int index) {
        byteIndex.setNumber(index);
    }

    /**
     * Return how much bites can picture store (capacity - stored files).
     * @param depthPerChannel
     * @return 
     */
    public long canStorebites() {
        if (chunkSize < 0 || chunkSize > 8) return -1;
        long capacity = 3*getwidth()*getHeight()*chunkSize;
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
    
    private void calculateIndexes () {
        indexOfPixel.setNumber(byteIndex.getNumber()*BYTE_LEN / (NUMBER_OF_CHANELS*chunkSize));
        indexOfChannel.setNumber((byteIndex.getNumber()*BYTE_LEN % (NUMBER_OF_CHANELS*chunkSize)) / (chunkSize));
        indexOfbite.setNumber((byteIndex.getNumber()*BYTE_LEN) % (chunkSize));
        /*
        System.out.format("Pixel: %d \t Chanel: %d \t bite: %d\n", 
                indexOfPixel.getNumber(), indexOfChannel.getNumber(), indexOfbite.getNumber());
        */
    }
    
    private byte loadNextByte () {
        int carry;
        int ret = 0;
        Pixel pixel;
        int channel = -1;
        int bit;
        int nthBit;
        calculateIndexes();
        for (int i = 0; i < BYTE_LEN; i++) {
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
            bit = nthBitFromRight(channel, this.chunkSize - indexOfbite.getNumber());
            
            nthBit = bit << BYTE_LEN-i-1;
            ret = ret | nthBit;
            
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
    
    private void storeNextByte (byte B) {
        int carry;
        Pixel pixel;
        int channel = -1;
        int bit;
        int nthBit;
        calculateIndexes();
        for (int i = 0; i < BYTE_LEN; i++) {
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
            nthBit = bit << (this.chunkSize - indexOfbite.getNumber());
            // write bite to pixel chanel
            channel = channel | nthBit;
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
            
            // move index to next bit
            carry = indexOfbite.add();
            carry = indexOfChannel.add(carry);
            if (indexOfPixel.add(carry) != 0) throw new IndexOutOfBoundsException();
        }
        // next
        byteIndex.add();
    }

    private boolean checkForHeader () {
        setByteIndex(0);
        StringBuilder pictureContent = new StringBuilder();
        char nextChar;
        for (int i = 0; i < HEADER_KEY.length(); i++) {
            nextChar = (char)(loadNextByte() & 0xFF);
            pictureContent.append(nextChar);
        }
        return HEADER_KEY.compareTo(pictureContent.toString()) == 0;
    }
    
    private boolean createAndStoreHeader () {
        // TODO check if key fit to file !!!
        byte[] array = HEADER_KEY.getBytes();
        for (byte B : array) {
            storeNextByte(B);
        }
        return true;
    }
    
    private int getFirstLinkByteIndex () {
        return HEADER_KEY.length();
    }
    
    public int getNumberOfStoredFiles () {
        if (!checkForHeader()) return -1;
        // TODO
        return -1;
    }

    public DataFile[] storedFiles() {
        DataFile[] dtfs = null;
        if (!checkForHeader()) {
            return dtfs;
        }
        // TODO
        return dtfs;
    }
    
    public int[] getStoredFileLinks () {
        if (!checkForHeader()) {
            return null;
        }
        int linkIndex;
        List<Integer> links = new ArrayList();
        // set ByteIndexToFirstLink
        linkIndex = getFirstLinkByteIndex();
        setByteIndex(linkIndex);
        // store first link index
        links.add(linkIndex);
        while ((linkIndex = loadNextInt()) != -1) {
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
    public int findStoredFile (int index) {
        int[] links = getStoredFileLinks ();
        if (links == null) return -1;
        if (index >= links.length) return -1;
        if (index == -1) index = links.length;
        return links[index];
    }
    
    private int addNextLink () {
        // TODO
        int ByteIndex = findStoredFile(-1); // return index of file link to chain
        return -1;
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
        
        // Header
        for (int i = 0; i < df.getHeaderSize(); i++) {
            B = df.getHeadByte(i);
            storeNextByte(B);
        }
        // Data
        for (int i = 0; i < df.getFileSize(); i++) {
            B = df.getDataByte(i);
            storeNextByte(B);
        }
        return true;
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
        p.setChunkSize(4);
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
