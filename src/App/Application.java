package App;

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
        while (!quit) {
            UI.loadFromPictureMenu(pictureFormat, picture, null);
            switch (UI.readInt()) {
                case 1:
                    UI.chosePictureFormat(PICTURE_FORMATS);
                    index = UI.readInt()-1;
                    if (index >= 0 && index < PICTURE_FORMATS.length)  {
                        pictureFormat = PICTURE_FORMATS[index];
                    } else UI.printInvaliInput();
                    break;
                case 2:
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
                case 3:
                    
                    break;
                case 4:
                    quit = true;
                    break;
                default:
                    UI.printInvaliInput();
            }
        }
        
    }
    
}