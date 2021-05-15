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
    private static DataFile dataFile;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int index;
        File[] files;
        DataFile[] dataFiles;
        while (!exit) {
            UI.loadFromPictureMenu(pictureFormat, picture, null);
            switch (UI.readInt()) {
                case 1: // Change format
                    UI.chosePictureFormat(PICTURE_FORMATS);
                    index = UI.readInt()-1;
                    if (index >= 0 && index < PICTURE_FORMATS.length)  {
                        pictureFormat = PICTURE_FORMATS[index];
                    } else UI.printInvaliInput();
                    break;
                case 2: // Change picture
                    files = UI.listAllPictires(dataDir, pictureFormat);
                    index = UI.readInt()-1;
                    if (index >= 0 && index < files.length) {
                        try {
                            picture = new Picture(files[index]);
                        } catch (IOException ex) {
                            assert false: "Implementation error!" + ex;
                        }
                    } else {
                        UI.printInvaliInput();
                    }
                    break;
                case 3: // Sort & print
                    
                    break;
                case 4: // Add file
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
                case 5: // Load File from picture
                    if (picture == null) {
                        UI.print("Obrázek není vybrán!");
                    } else {
                        //datafiles = picture.storedFiles();
                    }
                    break;
                case 6: // delete all records
                    if (picture == null) {
                        UI.print("Obrázek není vybrán!");
                    } else {
                        //picture.removeAllStored();
                    }
                    break;
                case 7: // Aply !
                    
                    break;
                case 8: // Quit
                    exit = true;
                    break;
                default:
                    UI.printInvaliInput();
            }
        }
        UI.printEnd();
    }
    
}