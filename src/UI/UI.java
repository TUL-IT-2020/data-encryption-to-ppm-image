package UI;

import Tools.ExtensionFilter;
import Logic.Picture;
import java.io.File;
import java.util.Scanner;

/**
 *
 * @author pytel
 */
public class UI {

    public static Scanner sc = new Scanner(System.in);
    
    public static int readInt() {
        int number = -1;
        if (sc.hasNextInt()) {
            number = sc.nextInt();
        }
        sc.nextLine();
        return number;
    }
    
    public static void printTypeChoice () {
        System.out.print("Zadej volbu: ");
    }

    public static void printInvaliInput() {
        System.out.println("Špatný vstup!");
    }

    public static void printEnd() {
        System.out.println("Aplikace ukončena.");
    }
    
    public static void printPictureNotSelected() {
        System.out.println("Obrázek není vybrán!");
    }
    
    public static void print (String s) {
        System.out.format("%s", s);
    }

    public static File[] listAllPictires(File dir, String extension) {
        File[] files;
        ExtensionFilter filter = new ExtensionFilter(extension);
        files = dir.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            System.out.format("\t %d) %s\n", i+1, files[i].getName());
        }        
        printTypeChoice();
        return files;
    }

    public static void loadFromPictureMenu(String format, Picture picture, int chunkSize, String sortingType) {
        System.out.println(" --- Hlavní menu ---");
        System.out.format("\t 1) Vybrat formát: %s\n", format);
        System.out.format("\t 2) Vybrat obrázek: %s\n", picture == null ? "není vybrán!" : picture.getName() + format);
        System.out.format("\t 3) Vybrat velikost chunku: %d.\n", chunkSize);
        System.out.format("\t 4) Vybrat typ třídění.\n");
        System.out.format("\t 5) Vypsat obsah dle: %s.\n", sortingType);
        System.out.format("\t 6) Přidat soubor do obrázku.\n");
        System.out.format("\t 7) Nahrát soubory z obrázku.\n");
        System.out.format("\t 8) Smazat záznamy.\n");
        System.out.format("\t 9) Uložit soubor.\n");
        System.out.format("\t 10) Uložit obrázek.\n");
        System.out.format("\t 11) Odejít.\n");
        printTypeChoice();
    }

    public static void chosePictureFormat(String[] PICTURE_FORMATS) {
        System.out.format("Kompatibilní formáty:\n");
        for (int i = 0; i < PICTURE_FORMATS.length; i++) {
            System.out.format("\t %d) %s\n", i+1, PICTURE_FORMATS[i]);
        }
        printTypeChoice();
    }
    
    public static String choseNewName () {
        System.out.print("Zadej nový název: ");
        String name = sc.nextLine();
        return name;
    }

    public static File[] listAllFiles(File dir) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.format("\t %d) %s\n", i+1, files[i].getName());
        }        
        printTypeChoice();
        return files;
    }
     
}
