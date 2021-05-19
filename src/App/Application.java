package App;

import Logic.DataFile;
import Logic.Picture;
import UI.UI;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pytel
 */
public class Application {

    public static String[] PICTURE_FORMATS = {".ppm"};
    public static String OUTPUT_FORMAT = ".ppm";
    public static int DEFAULT_PICTURE_FORMAT = 0;
    
    private static boolean exit = false;
    //public static File dataDir = new File(System.getProperty("user.dir") + "/Data/testDataSet");
    public static File dataDir = new File(System.getProperty("user.dir") + "/Data");
    private static String pictureFormat = PICTURE_FORMATS[DEFAULT_PICTURE_FORMAT];
    private static Picture picture = null;
    private static int chunkSize = 0;
    private static DataFile dataFile2store;
    private static DataFile[] storedFiles;
    
    private static void changeFormat() {
        int index;
        UI.chosePictureFormat(PICTURE_FORMATS);
        index = UI.readInt() - 1;
        if (index >= 0 && index < PICTURE_FORMATS.length) {
            pictureFormat = PICTURE_FORMATS[index];
        } else {
            UI.printInvaliInput();
        }
    }
    
    private static void changePicture() {
        int index;
        File[] files;
        files = UI.listAllPictires(dataDir, pictureFormat);
        index = UI.readInt() - 1;
        if (index >= 0 && index < files.length) {
            try {
                picture = new Picture(files[index]);
            } catch (IOException ex) {
                assert false : "Implementation error!" + ex;
            }
        } else {
            UI.printInvaliInput();
        }
    }
    
    private static void selectChunnkSize () {
        int min = 1;
        int max = 8;
        UI.print("Zadej volbu (" + min + "-" + max + "): ");
        int number = UI.readInt();
        if (number > min && number <= max) {
            try {
                picture.setChunkSize(number);
                chunkSize = picture.getChunkSize();
            } catch (IllegalArgumentException ex) {
                assert false : "Implementation error!" + ex;
            }
        } else {
            UI.printInvaliInput();
        }
    }
    
    private static void addFile () {
        int index;
        File[] files;
        DataFile[] dataFiles;
        files = UI.listAllFiles(dataDir);
        index = UI.readInt() - 1;
        if (picture == null) {
            UI.print("Obrázek není vybrán!");
            return;
        }
        if (index >= 0 && index < files.length) {
            try {
                dataFile2store = new DataFile(files[index]);
                if (picture.addFile(dataFile2store) == false) {
                    UI.print("Nelze provést! Soubor je příliš velký.");
                } else {
                    UI.print("Soubor byl uložen do obrázku.");
                }
            } catch (IOException ex) {
                assert false : "Implementation error!" + ex;
            }
        } else {
            UI.printInvaliInput();
        }
    }
    
    private static void loeadFilesFromPicture () {
        if (picture == null) {
            UI.print("Obrázek není vybrán!");
        } else {
            storedFiles = picture.storedFiles();
            UI.print("Data nahrány.");
        }
    }
    
    private static void deleteAllRecords () {
        if (picture == null) {
            UI.print("Obrázek není vybrán!");
        } else {
            picture.removeAllStored();
        }
    }
    
    private static void save2disk() {
        String newName = UI.choseNewPictureName() + OUTPUT_FORMAT;
        File newPicture = new File(dataDir, newName);
        try {
            picture.save2File(newPicture);
        } catch (IOException ex) {
            assert false : "Implementation ERROR!" + ex;
        }
        UI.print("Obrázek je uložen.");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        while (!exit) {
            UI.loadFromPictureMenu(pictureFormat, picture, chunkSize, null);
            switch (UI.readInt()) {
                case 1:
                    changeFormat();
                    break;
                case 2:
                    changePicture();
                    break;
                case 3:
                    selectChunnkSize();
                    break;
                case 4: // select sorting type
                    
                    break;
                case 5: // Sort & print
                    
                    break;
                case 6:
                    addFile();
                    break;
                case 7:
                    loeadFilesFromPicture();
                    break;
                case 8:
                    deleteAllRecords();
                    break;
                case 9:
                    save2disk();
                    break;
                case 10: // Quit
                    exit = true;
                    break;
                default:
                    UI.printInvaliInput();
            }
        }
        UI.printEnd();
    }
    
}