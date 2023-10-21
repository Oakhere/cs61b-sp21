package deque;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

public class MADTest {

    public class IntComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }
    @Test
    public void testOne() {
        IntComparator c = new IntComparator();
        MaxArrayDeque<Integer> madDeque = new MaxArrayDeque<>(c);
        madDeque.addFirst(1);
        madDeque.addFirst(2);
        madDeque.addLast(3);
        madDeque.addLast(4);
        madDeque.addFirst(5);
        madDeque.addFirst(6);
        madDeque.addLast(7);
        madDeque.addFirst(8);
        assertEquals((int) madDeque.max(), 8);
        // The deque is like this: 8 6 5 2 1 3 4 7
        madDeque.addLast(9);
        madDeque.addFirst(10);
        // The deque is like this: 10 8 6 5 2 1 3 4 7 9
        assertEquals((int) madDeque.max(c), 10);

    }
}
