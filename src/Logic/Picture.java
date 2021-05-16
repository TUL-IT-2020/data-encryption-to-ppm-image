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
    private static int NUMBER_OF_CHANELS = 3;
    
    private File path;
    private String name;
    private String format;
    
    private PictureDataInterface pictureDataAndInfo;
    
    private List<Pixel> data = new ArrayList();
    private Counter byteIndex;  // nth Byte
    private int chunkSize;
    
    private class Counter {
        private final int size;
        private int number = 0;

        public Counter(int size) {
            this.size = size;
        }

        public int getNumber() {
            return number;
        }

        public boolean setNumber(int number) {
            if (number >= size || number < 0) {
                return false;
            }
            this.number = number;
            return true;
        }
        
        public int add() {
            return add(1);
        }
        
        public int add(int number) {
            this.number = (this.number + number)%size;
            int carry = (this.number + number)/size;
            return carry;
        }
    }

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
    
    private void setChunkSize (int chunk) {
        this.chunkSize = chunk;
    }
    
    private void setByteIndex (int index) {
        byteIndex.setNumber(index);
    }
    
    private byte nextByte () {
        Counter indexOfPixel = new Counter(data.size()*NUMBER_OF_CHANELS);
        Counter indexOfChanel = new Counter(NUMBER_OF_CHANELS);
        Counter indexOfbite = new Counter(chunkSize);
        indexOfPixel.setNumber(byteIndex.getNumber()*8 / (NUMBER_OF_CHANELS*chunkSize));
        indexOfChanel.setNumber((byteIndex.getNumber()*8 % (NUMBER_OF_CHANELS*chunkSize)) / (chunkSize));
        indexOfbite.setNumber((byteIndex.getNumber()*8) % (chunkSize));
        System.out.format("Pixel: %d \t Chanel: %d \t bite: %d\n", 
                indexOfPixel.getNumber(), indexOfChanel.getNumber(), indexOfbite.getNumber());
        /*
        int indexOfPixel = byteIndex.getNumber()*8 / (3*chunkSize);
        int indexOfChanel = (byteIndex.getNumber()*8 % (3*chunkSize)) / (chunkSize);
        int indexOfbite = (byteIndex.getNumber()*8) % (chunkSize);
        System.out.format("Pixel: %d \t Chanel: %d \t bite: %d\n", indexOfPixel, indexOfChanel, indexOfbite);
        */
        // TODO
        byteIndex.add();
        return (byte)0;
    }
    
    private int nextInt () {
        return nextByte() << 8 | nextByte();
    }

    private boolean checkForHeader(int chunk) {
        setChunkSize(chunk);
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
    
    
    /**
     * Private metode test helper.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File path = new File(System.getProperty("user.dir") + "/Data/testDataSet");
        
        File picturePath = new File(path, "white.ppm");
        try {
            Picture p = new Picture(picturePath);
            p.setByteIndex(0);
            p.setChunkSize(2);
            p.nextByte();
            p.nextByte();
            p.nextByte();
            p.nextByte();
            p.nextByte();
            p.nextByte();
            p.nextByte();
            p.nextByte();
            p.nextByte();
        } catch (IOException ex) {
        }
        
    }
}
