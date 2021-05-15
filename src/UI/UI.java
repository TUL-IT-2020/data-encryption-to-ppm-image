package UI;

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

    public static void printMenu() {
        System.out.println(" --- Hlavní menu ---");
        System.out.println("\t 1) Načíst data z obrázku.");
        System.out.println("\t 2) Vložit data do obrázku.");
        System.out.println("\t 3) Ukončit.");
        System.out.print("Zadej volbu: ");
    }

    public static void printInvaliInput() {
        System.out.println("Špatný vstup!");
    }

    public static void printEnd() {
        System.out.println("Aplikace ukončena.");
    }

    public static File[] listAllPictires(File dir, String extension) {
        File[] files;
        ExtensionFilter filter = new ExtensionFilter(extension);
        files = dir.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            System.out.format("\t %d) %s\n", i+1, files[i].getName());
        }        
        System.out.print("Zadej volbu: ");
        return files;
    }
    
    public static void print (String s) {
        System.out.format("%s", s);
    }

    public static void loadFromPictureMenu(String format, Picture picture, String sortingType) {
        System.out.format("\t 1) Vybrat formát: %s.\n", format);
        System.out.format("\t 2) Vybrat obrázek: %s\n", picture == null ? "není vybrán!" : picture.getName() + format);
        System.out.format("\t 3) Vypsat obsah dle: %s.\n", sortingType);
        System.out.format("\t 4) Přidat soubor.\n");
        System.out.format("\t 5) Smazat záznamy.\n");
        System.out.format("\t 6) Provést/Uložit.\n");
        System.out.format("\t 7) Odejít.\n");
        System.out.print("Zadej volbu: ");
    }
    
    public static void loadToPictureMenu(String format, Picture picture, String sortingType) {
        System.out.format("\t 1) Vybrat formát: %s.\n", format);
        System.out.format("\t 2) Vybrat obrázek: %s\n", picture == null ? "není vybrán!" : picture.getName() + format);
        System.out.format("\t 3) Vypsat obsah dle: %s.\n", sortingType);
        System.out.format("\t 4) Přidat soubor.\n");
        System.out.format("\t 5) Smazat záznamy.\n");
        System.out.format("\t 6) Provést/Uložit.\n");
        System.out.format("\t 7) Odejít.\n");
        System.out.print("Zadej volbu: ");
    }

    public static void chosePictureFormat(String[] PICTURE_FORMATS) {
        System.out.format("Kompatibilní formáty:\n");
        for (int i = 0; i < PICTURE_FORMATS.length; i++) {
            System.out.format("\t %d) %s\n", i+1, PICTURE_FORMATS[i]);
        }
        System.out.print("Zadej volbu: ");
    }

    public static File[] listAllFiles(File dir) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.format("\t %d) %s\n", i+1, files[i].getName());
        }        
        System.out.print("Zadej volbu: ");
        return files;
    }
     
}
