package Tools;

import Logic.DataFile;
import java.util.Comparator;

/**
 *
 * @author pytel
 */
public class DataFileComparatorBySize  implements Comparator<DataFile> {
    
    @Override
    public int compare(DataFile dtf1, DataFile dtf2) {
        return Double.compare(dtf1.getGrossSize(), dtf2.getGrossSize());
    }
}
