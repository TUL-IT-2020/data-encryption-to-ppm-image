package Logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class stroting information to store/load from picture.
 * @author pytel
 */
public class DataFile {

    private File file;
    private String name;
    private String format;
    // TODO add time of file creation
    // TODO sort by size
    // TODO sort by time
    
    public static int BYTE_LENGHT = 8;
    private byte[] FileContent = null;
    private byte[] HeaderContent = null;

    /**
     * Load info about file to memory from disk.
     * @param filePath
     * @throws FileNotFoundException 
     */
    public DataFile(File filePath) throws FileNotFoundException {
        if (!filePath.exists()) throw new FileNotFoundException();
        this.file = filePath;
        this.name = filePath.getName().split("\\.")[0];
        this.format = "." + filePath.getName().split("\\.")[1];
        generateHeader();
    }
    
    /**
     * Load info about file to memory from picture.
     * @param header 
     */
    public DataFile(byte[] header) {
        //TODO
    }

    public String getName() {
        return name;
    }

    /**
     * Return file size in Bytes.
     * @return 
     */
    public long getFileSize() {
        return file.length();
    }
    
    public long getHeaderSize() {
        return this.HeaderContent.length;
    }
    
    public long getGrossSize() {
        return getHeaderSize() + getFileSize();
    }

    /**
     * Load file content to memory.
     */
    void ReadFile() throws FileNotFoundException, IOException {
        resetDataArrayPointers();
        
        // TODO change, nead lot of memory!!!
        // TODO asi to shoří na velký soubory: long -> int
        FileContent = new byte[(int)getFileSize()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(FileContent);
        }
    }
    
    private void generateHeader () {
        // TODO
        // Byte ArrayList
        // Int Header lenght 
        // Int Name lenght
        // Char[] Name
        // Int Format lenght
        // Char[] Format
        // Int Data lenght
        // Return ArrayList.toArray()
    }

    // TODO test it
    public byte getDataByte (int index) {
        return FileContent[index];
    }
    
    // TODO test it
    public byte getHeadByte (int index) {
        return FileContent[index];
    }

    @Override
    public String toString() {
        return "DataFile{" + "name=" + name + ", format=" + format + ", size=" + getFileSize() + '}';
    }
    
    // ---- Unused ----
    private int ByteIndex;
    private int BitIndex;
    
    private void resetDataArrayPointers () {
        BitIndex = 0;
        ByteIndex = 0;
    }
    
    private byte nthBitFromLeft(byte B, int index) {
        //System.out.format("Shift: %s \t last: %s\n", Integer.toString(B >> 7-index,2), Integer.toString((B >> 7-index) & 1,2));
        return (byte)((B >> 7-index) & 1);
    }
    
    private char byte2char (byte B) {
        return (char)(B & 0xFF);
    }
    
    /**
     * Return next n bites stored in Byte aligned to right.
     * @param chunkSize => n bites to read
     * @return 
     */
    public Byte getNextNbites(int chunkSize) {
        if (ByteIndex >= FileContent.length) return null;
        byte bitArray = 0;
        byte nthBit = 0;
        byte B;
        for (int i = 0; i < chunkSize; i++) {
            // TODO max index cap !!!
            if (BitIndex >= BYTE_LENGHT) {
                ByteIndex += 1;
                BitIndex = 0;
            }
            B = FileContent[ByteIndex];
            nthBit = (byte) (nthBitFromLeft(B, BitIndex) << chunkSize-i-1);
            bitArray = (byte) (bitArray | nthBit);
            //System.out.format("Index Byte: %d content: %s \t bit: %d content: %s \n", ByteIndex, byte2char(B), BitIndex, Integer.toString(nthBit & 0xFF,2));
            //System.out.format("Byte: %s\n", Integer.toString(bitArray & 0xFF,2));
            BitIndex += 1;
        }
        return bitArray;
    }

}