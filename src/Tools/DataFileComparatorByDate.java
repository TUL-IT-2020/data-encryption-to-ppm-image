package Tools;

import Logic.DataFile;
import java.util.Comparator;

/**
 *
 * @author pytel
 */
public class DataFileComparatorByDate  implements Comparator<DataFile> {
    
    @Override
    public int compare(DataFile dtf1, DataFile dtf2) {
        if (dtf1.getLastModified() == dtf2.getLastModified()) {
            return 0;
        } else if (dtf1.getLastModified() > dtf2.getLastModified()) {
            return +1;
        }
        return -1;
    }
}
