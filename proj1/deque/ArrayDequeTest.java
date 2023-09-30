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
    @Test
    public void getTestOne() {
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
        assertEquals((int)adeque.get(1), 1);
    }
    @Test
    public void getTestTwo() {
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
        adeque.addLast(2);
        assertEquals((int)adeque.get(1), 1);
    }
    @Test
    public void getTest() {
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
        adeque.addFirst(2);
        adeque.addLast(3);
        adeque.addLast(4);
        adeque.addFirst(5);
        adeque.addFirst(6);
        // The deque is like this: 6 5 2 1 3 4
        assertEquals((int)adeque.get(4), 1);
        assertEquals((int)adeque.get(1), 6);
        assertEquals((int)adeque.get(6), 4);
    }

    @Test
    public void fillUpThenEmpty() {
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
        adeque.addFirst(2);
        adeque.addLast(3);
        adeque.addLast(4);
        adeque.addFirst(5);
        adeque.addFirst(6);
        adeque.addLast(7);
        adeque.addLast(8);
        // The deque is like this: 6 5 2 1 3 4 7 8
        adeque.removeFirst();
        adeque.removeFirst();
        adeque.removeFirst();
        adeque.removeFirst();
        assertEquals((int)adeque.removeFirst(), 3);
        assertEquals((int)adeque.removeFirst(), 4);
        assertEquals((int)adeque.removeFirst(), 7);
        assertEquals((int)adeque.removeFirst(), 8);
    }
}
