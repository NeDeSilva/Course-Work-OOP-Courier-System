package Test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicTest {

    @Test
    void testEquality() {
        assertEquals(2, 1 + 1, "1 + 1 should equal 2");
    }

    @Test
    void testTrueCondition() {
        assertTrue(5 > 3, "5 should be greater than 3");
    }

    @Test
    void testNullCheck() {
        Object obj = null;
        assertNull(obj, "Object should be null");
    }
}