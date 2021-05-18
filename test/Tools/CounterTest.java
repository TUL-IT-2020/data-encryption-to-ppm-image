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
        int size = 10;
        Counter c = new Counter(size);
        Assert.assertNotNull("Creation failed!", c);
        //assertEquals(number, -1);
    }
    
    @Test
    public void emptyCounter() {
        int size = 10;
        Counter c = new Counter(size);
        assertEquals(c.getNumber(), 0);
    }
    
    @Test
    public void addOne() {
        int size = 10;
        Counter c = new Counter(size);
        c.add();
        assertEquals(c.getNumber(), 1);
    }
    
    @Test
    public void addTwo() {
        int size = 10;
        Counter c = new Counter(size);
        c.add(2);
        assertEquals(c.getNumber(), 2);
    }
    
    @Test
    public void setNumber_Valid() {
        int size = 10;
        Counter c = new Counter(size);
        Assert.assertTrue(c.setNumber(5));
        assertEquals(c.getNumber(), 5);
    }
    
    @Test
    public void setNumber_Invalid() {
        int size = 10;
        Counter c = new Counter(size);
        Assert.assertFalse(c.setNumber(12));
        assertEquals(c.getNumber(), 0);
    }
    
    @Test
    public void carryAfterAdding() {
        int size = 2;
        Counter c = new Counter(size);
        assertEquals(c.getNumber(), 0);
        c.add();
        assertEquals(c.getNumber(), 1);
        c.add();
        assertEquals(c.getNumber(), 0);
    }
    
}
