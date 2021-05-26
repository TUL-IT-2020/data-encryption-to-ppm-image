package Tools;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pytel
 */
public class CounterTest {
    
    public CounterTest() {
    }
    
    @Test
    public void createCounter() {
        System.out.format("Create test: \t");
        int maxSize = 1000;
        Counter c;
        for (int size = 0; size < maxSize; size++) {
            c = new Counter(size);
            Assert.assertNotNull("Creation failed!", c);
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void emptyCounter() {
        System.out.format("Is empty: \t");
        int maxSize = 100;
        Counter c;
        for (int size = 0; size < maxSize; size++) {
            c = new Counter(size);
            assertEquals(c.getNumber(), 0);
        }
        System.out.format("Done\n");
    }
    
    @Test
    public void addOne() {
        System.out.format("Add one: \t");
        int size = 10;
        Counter c = new Counter(size);
        c.add();
        assertEquals(c.getNumber(), 1);
        System.out.format("Done\n");
    }
    
    @Test
    public void addTwo() {
        System.out.format("Add two: \t");
        int size = 10;
        Counter c = new Counter(size);
        c.add(2);
        assertEquals(c.getNumber(), 2);
        System.out.format("Done\n");
    }
    
    @Test
    public void setNumber_Valid() {
        System.out.format("Set valid number: \t");
        int size = 10;
        Counter c = new Counter(size);
        Assert.assertTrue(c.setNumber(5));
        assertEquals(c.getNumber(), 5);
        System.out.format("Done\n");
    }
    
    @Test
    public void setNumber_Invalid() {
        System.out.format("Set invalid number: \t");
        int size = 10;
        Counter c = new Counter(size);
        Assert.assertFalse(c.setNumber(12));
        assertEquals(c.getNumber(), 0);
        System.out.format("Done\n");
    }
    
    @Test
    public void counterOwerfloowAfterAdding() {
        System.out.format("Carry: \t");
        int size = 2;
        Counter c = new Counter(size);
        assertEquals(c.getNumber(), 0);
        c.add();
        assertEquals(c.getNumber(), 1);
        c.add();
        assertEquals(c.getNumber(), 0);
        c.add();
        assertEquals(c.getNumber(), 1);
        c.add();
        assertEquals(c.getNumber(), 0);
        System.out.format("Done\n");
    }
    
    @Test
    public void carryAfterAdding() {
        System.out.format("More carry after adding: \t");
        int Jim_Carry;
        int maxSize = 100;
        Counter c;
        for (int size = 1; size < maxSize; size++) {
            c = new Counter(size);
            Jim_Carry = 0;
            //System.out.format("Carry: %d \t number: %d \n", Jim_Carry, c.getNumber());
            c.add();
            for (int i = 1; i < 3 * size; i++) {
                Jim_Carry = c.add();
                assertEquals((i + 1) % size == 0 ? 1 : 0, Jim_Carry);
                //System.out.format("Carry: %d \t number: %d \n", Jim_Carry, c.getNumber());
            }
        }
        System.out.format("Done\n");
    }
    
}
