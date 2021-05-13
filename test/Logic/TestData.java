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
            new File(System.getProperty("user.dir") + "/Data/"),
            "small", ".ppm", 2, 3),
        new TestPictureData(
            new File(System.getProperty("user.dir") + "/Data/"),
            "white", ".ppm", 10, 10),
        new TestPictureData(
            new File(System.getProperty("user.dir") + "/Data/"),
            "Face-smile", ".ppm", 50, 50)
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
}
