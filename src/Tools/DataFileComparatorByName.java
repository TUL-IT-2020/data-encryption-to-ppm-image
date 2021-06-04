package Tools;

import Logic.DataFile;
import java.util.Comparator;

/**
 *
 * @author pytel
 */
public class DataFileComparatorByName  implements Comparator<DataFile> {
    
    @Override
    public int compare(DataFile dtf1, DataFile dtf2) {
        return dtf1.getName().compareTo(dtf2.getName());
    }
}
