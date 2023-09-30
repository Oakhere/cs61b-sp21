package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addFirstTest(){
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
    }

    @Test
    public void removeFirstTest() {
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
        assertEquals((int)adeque.removeFirst(), 1);
    }

    @Test
    public void removeLastTest() {
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
        assertEquals((int)adeque.removeLast(), 1);
    }

    /** Add first twice, and remove first then remove last. */
    @Test
    public void addFirstTwiceThenRemoveTwice(){
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
        adeque.addFirst(2);
        assertEquals((int)adeque.removeFirst(), 2);
        assertEquals((int)adeque.removeLast(), 1);

    }
}
