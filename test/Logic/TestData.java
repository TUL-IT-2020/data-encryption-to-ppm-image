package Logic;

import java.io.File;

/**
 * This is class with data source for testing.
 * @author pytel
 */
public class TestData {
    
    /**
     * Pictures for testing.
     */
    public static TestPictureData[] pictures = {
        new TestPictureData(
            new File(System.getProperty("user.dir") + "/data/testDataSet"),
            "small", ".ppm", 2, 3),
        new TestPictureData(
            new File(System.getProperty("user.dir") + "/data/testDataSet"),
            "white", ".ppm", 10, 10),
        new TestPictureData(
            new File(System.getProperty("user.dir") + "/data/testDataSet"),
            "Face-smile", ".ppm", 50, 50),
        new TestPictureData(
            new File(System.getProperty("user.dir") + "/data/testDataSet"),
            "adventuretime", ".ppm", 727, 940)
    };
    
    static class TestPictureData {
        public File path;
        public String name;
        public String format;
        public int height;
        public int width;
        File picturePath;

        public TestPictureData(File path, String name, String format, int height, int width) {
            this.path = path;
            this.name = name;
            this.format = format;
            this.height = height;
            this.width = width;
            picturePath = new File(path, name + format);
        }
    }
    
    /**
     * Files for testing.
     */
    public static TestFileData[] files = {
        new TestFileData(
                new File(System.getProperty("user.dir") + "/data/testDataSet"),
                "test", ".txt", 6),
        new TestFileData(
                new File(System.getProperty("user.dir") + "/data/testDataSet"),
                "soubor1", ".txt", 5),
        new TestFileData(
                new File(System.getProperty("user.dir") + "/data/testDataSet"),
                "dalsi_soubor", ".txt", 272),
        /*new TestFileData(
                new File(System.getProperty("user.dir") + "/data/testDataSet"),
                "f4", ".txt", 12257),*/
        new TestFileData(
                new File(System.getProperty("user.dir") + "/data/testDataSet"),
                "f5", ".pdf", 864297)
    };
    
    static class TestFileData {
        public File path;
        public String name;
        public String format;
        public File filePath;
        public long size;

        public TestFileData(File path, String name, String format, long size) {
            this.path = path;
            this.name = name;
            this.format = format;
            this.filePath = new File(path, name + format); 
            this.size = size;
        }
    }
}
