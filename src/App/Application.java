package App;

import Logic.DataFile;
import Logic.Picture;
import Tools.DataFileComparatorByDate;
import Tools.DataFileComparatorByName;
import Tools.DataFileComparatorBySize;
import UI.UI;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author pytel
 */
public class Application {

    public static final String[] PICTURE_FORMATS = {".ppm"};
    public static String OUTPUT_FORMAT = ".ppm";
    public static final int DEFAULT_PICTURE_FORMAT = 0;
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    public static final int DEFAULT_COMPARATOR = 0;
    private static final Comparator[] COMPARATORS = {
        new DataFileComparatorByName(),
        new DataFileComparatorBySize(),
        new DataFileComparatorByDate(),
    };
    private static final String[] COMPARATORS_NAMES_CZ = {
        "jména",
        "velikosti",
        "data",
    };
    
    public static File dataDir = new File(System.getProperty("user.dir") + "/data/testDataSet");
    //public static File dataDir = new File(System.getProperty("user.dir") + "/data");
    private static String pictureFormat = PICTURE_FORMATS[DEFAULT_PICTURE_FORMAT];
    private static int comparatorIndex = DEFAULT_COMPARATOR;
    private static Picture picture = null;
    private static int chunkSize = 0;
    private static DataFile[] storedFiles;
          
    private static void changeFormat() {
        int index;
        UI.choseFrom("Kompatibilní formáty:", PICTURE_FORMATS);
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
        files = UI.listAllPictures(dataDir, pictureFormat);
        index = UI.readInt() - 1;
        if (index >= 0 && index < files.length) {
            try {
                picture = new Picture(files[index]);
                if (chunkSize != 0) picture.setChunkSize(chunkSize);
            } catch (IOException ex) {
                assert false : "Implementation error!" + ex;
            }
        } else {
            UI.printInvaliInput();
        }
    }
    
    private static void selectChunkSize () {
        int min = 1;
        int max = 8;
        UI.print("Zadej volbu (" + min + "-" + max + "): ");
        int number = UI.readInt();
        if (number >= min && number <= max) {
            chunkSize = number;
            try {
                if (picture != null) picture.setChunkSize(number);
            } catch (IllegalArgumentException ex) {
                assert false : "Implementation error!" + ex;
            }
        } else {
            UI.printInvaliInput();
        }
    }
    
    private static void addFile () {
        if (picture == null) {
            UI.print("Obrázek není vybrán!");
            return;
        }
        if (chunkSize == 0) {
            UI.print("Velikost chunku není zvolena!\n");
            return;
        }
        int index;
        File[] files;
        DataFile dataFile2store;
        files = UI.listAllFiles(dataDir);
        index = UI.readInt() - 1;
        if (index >= 0 && index < files.length) {
            try {
                dataFile2store = new DataFile(files[index]);
                if (picture.addFile(dataFile2store) == false) {
                    UI.print("Nelze provést! Soubor je příliš velký.\n");
                } else {
                    UI.print("Soubor byl uložen do obrázku.\n");
                }
            } catch (IOException ex) {
                assert false : "Implementation error!" + ex;
            }
        } else {
            UI.printInvaliInput();
        }
    }
    
    private static void loadFilesFromPicture () {
        if (picture == null) {
            UI.printPictureNotSelected();
        } else if (chunkSize == 0) {
            UI.print("Chybí velikost chuknu!\n");
        } else {
            storedFiles = picture.storedFiles();
            UI.print("Data nahrány.\n");
        }
    }
    
    private static void deleteAllRecords () {
        if (picture == null) {
            UI.printPictureNotSelected();
        } else {
            picture.removeAllStored();
        }
    }
    
    private static void save2disk() {
        if (picture == null) { 
            UI.printPictureNotSelected();
            return;
        }
        String newName = UI.choseNewName() + OUTPUT_FORMAT;
        File newPicture = new File(dataDir, newName);
        try {
            picture.save2File(newPicture);
        } catch (IOException ex) {
            assert false : "Implementation ERROR!" + ex;
        }
        UI.print("Obrázek je uložen.\n");
    }
    
    private static void printFiles () {
        if (storedFiles == null) {
            UI.print("Žádné soubory k tisku.\n");
            return;
        }
        String s;
        LocalDate date;
        for (int i = 0; i < storedFiles.length; i++) {
            date = Instant.ofEpochMilli(storedFiles[i].getLastModified())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            s = String.format("\t %d) %15.15s%s o velikosti: %d B \tzměněno: %s\n",
                    i + 1,
                    storedFiles[i].getName(),
                    storedFiles[i].getFormat(),
                    storedFiles[i].getDataSize(),
                    date.format(dateFormat)
            );
            UI.print(s);
        }
    }

    private static void selectComparator() {
        int index;
        UI.choseFrom("Způsoby třídění:", COMPARATORS_NAMES_CZ);
        index = UI.readInt() - 1;
        if (index >= 0 && index < COMPARATORS.length) {
            comparatorIndex = index;
        } else {
            UI.printInvaliInput();
        }
    }
    
    private static void sortFilesAndPrint () {
        if (picture == null) { 
            UI.printPictureNotSelected();
            return;
        }
        if (chunkSize == 0) {
            UI.print("Velikost chunku není zvolena!\n");
            return;
        }
        if (storedFiles == null) {
            loadFilesFromPicture();
        }
        Arrays.sort(storedFiles, COMPARATORS[comparatorIndex]);
        printFiles();
    }
    
    private static void saveDataFileFromPicture () {
        if (picture == null) { 
            UI.printPictureNotSelected();
            return;
        }
        sortFilesAndPrint();
        UI.printTypeChoice();
        int index = UI.readInt() - 1;
        if (index < 0 || index >= storedFiles.length) {
            UI.printInvaliInput();
            return;
        }
        String newName = UI.choseNewName() + storedFiles[index].getFormat();
        File newFile = new File(dataDir, newName);
        try {
            storedFiles[index].save2File(newFile);
        } catch (IOException ex) {
            assert false : "Implementation ERROR!" + ex;
        }
        UI.print("Soubor je uložen.\n");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            UI.loadFromPictureMenu(pictureFormat, picture, chunkSize, COMPARATORS_NAMES_CZ[comparatorIndex]);
            switch (UI.readInt()) {
                case 1:
                    changeFormat();
                    break;
                case 2:
                    changePicture();
                    break;
                case 3:
                    selectChunkSize();
                    break;
                case 4:
                    selectComparator();
                    break;
                case 5:
                    sortFilesAndPrint();
                    break;
                case 6:
                    addFile();
                    break;
                case 7:
                    loadFilesFromPicture();
                    break;
                case 8:
                    deleteAllRecords();
                    break;
                case 9:
                    saveDataFileFromPicture();
                    break;
                case 10:
                    save2disk();
                    break;
                case 11: // Quit
                    exit = true;
                    break;
                default:
                    UI.printInvaliInput();
            }
        }
        UI.printEnd();
    }
    
}