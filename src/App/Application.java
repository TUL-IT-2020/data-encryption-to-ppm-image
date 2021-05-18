package App;

import Logic.DataFile;
import Logic.Picture;
import UI.UI;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author pytel
 */
public class Application {

    public static String[] PICTURE_FORMATS = {".ppm"};
    public static int DEFAULT_PICTURE_FORMAT = 0;
    
    private static boolean exit = false;
    public static File dataDir = new File(System.getProperty("user.dir") + "/Data/testDataSet");
    private static String pictureFormat = PICTURE_FORMATS[DEFAULT_PICTURE_FORMAT];
    private static Picture picture = null;
    private static int chunkSize = 0;
    private static DataFile dataFile;
    
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int index;
        File[] files;
        DataFile[] dataFiles;
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
                case 4: // Sort & print
                    
                    break;
                case 5: // Add file
                    files = UI.listAllFiles(dataDir);
                    index = UI.readInt()-1;
                    if (picture == null) {
                        UI.print("Obrázek není vybrán!");
                        break;
                    }
                    if (index >= 0 && index < files.length) {
                        try {
                            dataFile = new DataFile(files[index]);
                            // TODO check remaining free space in pisture !!!
                            //picture.addFile(dataFile);
                        } catch (IOException ex) {
                            assert false: "Implementation error!" + ex;
                        }
                    } else {
                        UI.printInvaliInput();
                    }
                    break;
                case 6: // Load File from picture
                    if (picture == null) {
                        UI.print("Obrázek není vybrán!");
                    } else {
                        //datafiles = picture.storedFiles();
                    }
                    break;
                case 7: // delete all records
                    if (picture == null) {
                        UI.print("Obrázek není vybrán!");
                    } else {
                        //picture.removeAllStored();
                    }
                    break;
                case 8: // Aply !
                    
                    break;
                case 9: // Quit
                    exit = true;
                    break;
                default:
                    UI.printInvaliInput();
            }
        }
        UI.printEnd();
    }
    
}