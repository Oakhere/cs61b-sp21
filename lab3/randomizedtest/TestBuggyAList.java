package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> buggyAList = new BuggyAList<>();
        AListNoResizing<Integer> simpleAList = new AListNoResizing<>();
        buggyAList.addLast(4);
        buggyAList.addLast(5);
        buggyAList.addLast(6);
        simpleAList.addLast(4);
        simpleAList.addLast(5);
        simpleAList.addLast(6);

        assertEquals(buggyAList.size(), simpleAList.size());
        assertEquals(buggyAList.removeLast(), simpleAList.removeLast());
        assertEquals(buggyAList.removeLast(), simpleAList.removeLast());
        assertEquals(buggyAList.removeLast(), simpleAList.removeLast());
    }
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> buggyL = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                buggyL.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                assertEquals(L.size(), buggyL.size());
            } else if (operationNumber == 2) {
                // getLast
                if (L.size() <= 0 || buggyL.size() <= 0) {
                    continue;
                }
                assertEquals(L.getLast(), buggyL.getLast());
            } else if (operationNumber == 3) {
                // removeLast
                if (L.size() <= 0 || buggyL.size() <= 0) {
                    continue;
                }
                assertEquals(L.removeLast(), buggyL.removeLast());
            }
        }
    }
}
