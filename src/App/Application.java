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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        while (!exit) {
            UI.printMenu();
            switch (UI.readInt()) {
                case 1:
                    loadFromPicture();
                    break;
                case 2:
                    
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    UI.printInvaliInput();
            }
        }
        UI.printEnd();
    }

    private static void loadFromPicture() {
        boolean quit = false;
        int index;
        File[] files;
        DataFile dataFile;
        while (!quit) {
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
                case 5: // delete all records
                    if (picture == null) {
                        UI.print("Obrázek není vybrán!");
                    } else {
                        //picture.removeAllStored();
                    }
                    break;
                case 6: // Aply !
                    
                    break;
                case 7: // Quit
                    quit = true;
                    break;
                default:
                    UI.printInvaliInput();
            }
        }
        
    }
    
}