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
        assertEquals((int)adeque.get(0), 1);
    }
    @Test
    public void getTestTwo() {
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
        adeque.addLast(2);
        assertEquals((int)adeque.get(1), 2);
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
        adeque.addLast(7);
        adeque.addFirst(8);
        // The deque is like this: 8 6 5 2 1 3 4 7
        adeque.addLast(9);
        adeque.addFirst(10);
        // The deque is like this: 10 8 6 5 2 1 3 4 7 9
        assertEquals((int)adeque.get(3), 5);
        assertEquals((int)adeque.get(0), 10);
        assertEquals((int)adeque.get(5), 1);
        assertEquals((int)adeque.get(8), 7);
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
    @Test
    public void fillUpThenEmptyTwo() {
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(1);
        adeque.addFirst(2);
        adeque.addLast(3);
        adeque.addLast(4);
        adeque.addFirst(5);
        adeque.addFirst(6);
        adeque.addLast(7);
        adeque.addLast(8);
        adeque.addFirst(9);
        adeque.addLast(10);
        // The deque is like this: 9 6 5 2 1 3 4 7 8 10
        adeque.removeFirst();
        adeque.removeFirst();
        adeque.removeFirst();
        adeque.removeFirst();
        adeque.removeLast();
        // 1 3 4 7 8
        assertEquals((int)adeque.removeFirst(), 1);
        assertEquals((int)adeque.removeLast(), 8);
        assertEquals((int)adeque.removeFirst(), 3);
        assertEquals((int)adeque.removeFirst(), 4);
        assertEquals((int)adeque.removeLast(), 7);
    }

    @Test
    public void addFirstRemoveLastTest() {
        ArrayDeque<Integer> adeque = new ArrayDeque<>();
        adeque.addFirst(0);
        adeque.addFirst(1);
        adeque.addFirst(2);
        adeque.addFirst(3);
        adeque.addFirst(4);
        adeque.addFirst(5);
        adeque.addFirst(6);
        assertEquals((int) adeque.removeLast(), 0);
        adeque.addFirst(8);
        adeque.addFirst(9);

    }


}
