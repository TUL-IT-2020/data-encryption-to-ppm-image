package Tools;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filter out files with out valid extension.
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
