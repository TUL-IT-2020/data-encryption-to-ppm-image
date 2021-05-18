package Tools;

/**
 *
 * @author pytel
 */
public class Counter {

    private final int size;
    private int number = 0;

    public Counter(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public boolean setNumber(int number) {
        if (number >= size || number < 0) {
            return false;
        }
        this.number = number;
        return true;
    }

    public int add() {
        return add(1);
    }

    public int add(int number) {
        int carry = (this.number + number) / size;
        this.number = (this.number + number) % size;        
        return carry;
    }
}
