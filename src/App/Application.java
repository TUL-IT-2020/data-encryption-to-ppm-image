package App;

import UI.UI;
import java.util.Scanner;

/**
 *
 * @author pytel
 */
public class Application {

    public static boolean quit = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        while (!quit) {
            UI.printMenu();
            switch (UI.readInt()) {
                case 1:
                    
                    break;
                case 2:
                    
                    break;
                case 3:
                    quit = true;
                    break;
                default:
                    UI.printInvaliInput();
            }
        }
        UI.printEnd();
    }
    
}