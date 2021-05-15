package UI;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author pytel
 */
public class ExtensionFilter implements FilenameFilter {

    private final String extension;

    public ExtensionFilter(String extension) {
        this.extension = extension;
    }
    
    @Override
    public boolean accept(File file, String name) {
        return name.endsWith(extension);
    }
}
