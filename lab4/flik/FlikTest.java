package flik;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlikTest {
    @Test
    public void sameTest() {
        int a = 0, b = 0;
        assertTrue(Flik.isSameNumber(a, b));
        for (int i = 0, j = 0; i < 300 && j < 300; i++, j++) {
            assertTrue(Flik.isSameNumber(i, j));
        }
    }

}
