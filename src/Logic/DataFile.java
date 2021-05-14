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
    
    private int ByteIndex;
    private int BitIndex;
    private byte[] FileContent = null;

    /**
     * Load info about file to memory.
     * @param filePath
     * @throws FileNotFoundException 
     */
    public void loadFile(File filePath) throws FileNotFoundException {
        if (!filePath.exists()) throw new FileNotFoundException();
        this.file = filePath;
        this.name = filePath.getName().split("\\.")[0];
        this.format = "." + filePath.getName().split("\\.")[1];
    }

    public String getName() {
        return name;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    /**
     * Return file size in Bytes.
     * @return 
     */
    public long getFileSize() {
        return file.length();
    }

    /**
     * Load file content to memory.
     */
    void ReadFile() throws FileNotFoundException, IOException {
        // TODO change, nead lot of memory!!!
        // TODO asi to shoří na velký soubory: long -> int
        
        // TODO try with resorcis
        FileContent = new byte[(int)getFileSize()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(FileContent);
        fis.close();
    }

    
}
