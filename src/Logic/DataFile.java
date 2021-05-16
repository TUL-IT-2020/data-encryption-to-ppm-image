package Logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static Tools.ByteTools.*;

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
    private Byte[] HeaderContent = null;

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
        try {
            ReadFile();
            generateHeader();
        } catch (IOException ex) {
            assert true : "File / implementation error.";
        }
    }
    
    /**
     * Load info about file to memory from picture. 
     * @param header 
     */
    public DataFile(byte[] header) {
        //TODO
        List<Byte> Bytes = new ArrayList();
        add2List(Bytes, header);
        
        // name lenght
        int stringLenght = nextInt(Bytes);
        // name string
        this.name = nextString(Bytes, stringLenght);
        
        // format lenght
        stringLenght = nextInt(Bytes);
        // format string
        this.format = nextString(Bytes, stringLenght);
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
    private void ReadFile() throws FileNotFoundException, IOException {
        resetDataArrayPointers();
        
        // TODO change, nead lot of memory!!!
        // TODO asi to shoří na velký soubory: long -> int
        FileContent = new byte[(int)getFileSize()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(FileContent);
        }
    }
    
    private void add2List (List list, byte[] bytes) {
        for (byte B : bytes) {
            list.add(B);
        }
    }
    
    private void generateHeader () {
        byte[] array;
        // Byte ArrayList
        List<Byte> Bytes = new ArrayList();
        
        // Int Header lenght
        // --- first index !!!
        
        // Int Data lenght
        add2List(Bytes, int2Bytes((int)this.getFileSize()));
        
        // Int Name lenght
        array = this.name.getBytes();
        add2List(Bytes, int2Bytes(array.length));
        // Char[] Name
        add2List(Bytes, array);
        
        // Int Format lenght
        array = this.format.getBytes();
        add2List(Bytes, int2Bytes(array.length));
        // Char[] Format
        add2List(Bytes, array);
        
        // insert Header lenght to begining
        byte[] headerLenght = int2Bytes(Bytes.size() + 4);   // 4 - Int lenght
        for (int i = 0; i < headerLenght.length; i++) {
            Bytes.add(i, headerLenght[i]);
        }
        
        // Return ArrayList.toArray()
        HeaderContent = Bytes.toArray(new Byte[Bytes.size()]);
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