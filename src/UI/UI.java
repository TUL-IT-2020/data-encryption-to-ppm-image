package UI;

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
        System.out.println("Hlavní menu");
        System.out.println("\t 1) Načíst data z obrázku.");
        System.out.println("\t 2) Vložit data do obrázku.");
        System.out.println("\t 3) Odejít.");
        System.out.print("Zadej volbu: ");
    }

    public static void printInvaliInput() {
        System.out.println("Špatný vstup!");
    }

    public static void printEnd() {
        System.out.println("Aplikace ukončena.");
    }
     
}
