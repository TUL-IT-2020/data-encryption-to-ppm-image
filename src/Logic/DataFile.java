package Logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static Tools.ByteTools.*;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class stroting information to store/load from picture.
 * @author pytel
 */
public class DataFile implements Comparable <DataFile> {

    private File file;
    private String name;
    private String format;
    private long lastModified;
    // TODO sort by size
    // TODO sort by time
    
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
        this.lastModified = filePath.lastModified();
        try {
            ReadFile();
            generateHeader();
        } catch (IOException ex) {
            assert false : "File / implementation error.";
        }
    }
    
    /**
     * Load info about file to memory from picture. 
     * @param header 
     * @param data 
     */
    public DataFile (byte[] header, byte[] data) {
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
        
        // last modifikation date
        this.lastModified = nextLong(Bytes);
        
        // store data
        FileContent = data;
        
        // generate header for evaluation
        generateHeader();
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public long getLastModified() {
        return lastModified;
    }

    /**
     * @return data size in Bytes.
     */
    public long getDataSize() {
        if (FileContent == null) return -1;
        return FileContent.length;
    }
    
    /**
     * @return file size in Bytes.
     */
    public long getFileSize() {
        if (file == null) return -1;
        return file.length();
    }
    
    /**
     * @return header size in Bytes.
     */
    public long getHeaderSize() {
        if (HeaderContent == null) return -1;
        return HeaderContent.length;
    }
    
    /**
     * @return header + data size in Bytes.
     */
    public long getGrossSize() {
        return getHeaderSize() + getDataSize();
    }

    /**
     * Load file content to memory.
     */
    private void ReadFile() throws FileNotFoundException, IOException {
        // TODO asi to shoří na velký soubory: long -> int
        FileContent = new byte[(int)getFileSize()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(FileContent);
        }
    }
    
    /**
     * Create header from specific format.
     */
    private void generateHeader () {
        byte[] array;
        // Byte ArrayList
        List<Byte> Bytes = new ArrayList();
        
        // Int Header lenght
        // --- first index !!!
        
        // Int Data lenght
        add2List(Bytes, int2Bytes((int)this.getDataSize()));
        
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
        
        // long date
        array = long2Bytes(this.lastModified);
        add2List(Bytes, array);
        
        // insert Header lenght to begining
        byte[] headerLenght = int2Bytes(Bytes.size() + 4);   // 4 - Int lenght
        for (int i = 0; i < headerLenght.length; i++) {
            Bytes.add(i, headerLenght[i]);
        }
        
        // Return ArrayList.toArray()
        HeaderContent = Bytes.toArray(new Byte[Bytes.size()]);
    }

    public byte getDataByte (int index) {
        return FileContent[index];
    }
    
    public byte getHeadByte (int index) {
        return HeaderContent[index];
    }
    
    public void save2File(File path) throws FileNotFoundException, IOException {
        try (OutputStream os = new FileOutputStream(path)) {
            os.write(FileContent);
        }
        path.setLastModified(lastModified);
    }

    @Override
    public String toString() {
        Date date = new Date(lastModified);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        String strDate = dateFormat.format(date);
        return "DataFile{" + "name=" + name + ", format=" + format + ", size=" + getDataSize() + ", date=" + strDate + '}';
    }

    /**
     * Comparing by file name.
     * @param dtf
     * @return 
     */
    @Override
    public int compareTo(DataFile dtf) {
        return this.name.compareTo(dtf.name);
    }

}