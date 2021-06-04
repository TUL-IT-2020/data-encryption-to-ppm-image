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
        if (dtf1.getGrossSize() == dtf2.getGrossSize()) {
            return 0;
        } else if (dtf1.getGrossSize() > dtf2.getGrossSize()) {
            return +1;
        }
        return -1;
    }
}
