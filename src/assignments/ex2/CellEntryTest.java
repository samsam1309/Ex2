package assignments.ex2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellEntryTest {

    @Test
    void testIsValid() {
        CellEntry cellEntry = new CellEntry(1, 2);
        assertTrue(cellEntry.isValid());
        CellEntry invalidEntry = new CellEntry(-1, 20);
        assertFalse(invalidEntry.isValid());
    }

    @Test
    void testGetX() {
        CellEntry cellEntry = new CellEntry(5, 10);
        assertEquals(5, cellEntry.getX());
    }

    @Test
    void testGetY() {
        CellEntry cellEntry = new CellEntry(5, 10);
        assertEquals(10, cellEntry.getY());
    }

    @Test
    void testToString() {
        CellEntry cellEntry = new CellEntry(0, 5);
        assertEquals("A5", cellEntry.toString());
    }
}
