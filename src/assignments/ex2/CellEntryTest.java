package assignments.ex2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellEntryTest {

    @Test
    void testIsValid() {
        // Valid CellEntry within the spreadsheet bounds
        CellEntry cellEntry = new CellEntry(1, 2);
        assertTrue(cellEntry.isValid());

        // Invalid CellEntry with negative indices
        CellEntry invalidEntry1 = new CellEntry(-1, 2);
        assertFalse(invalidEntry1.isValid());

        // Invalid CellEntry exceeding width
        CellEntry invalidEntry2 = new CellEntry(Ex2Utils.WIDTH, 2);
        assertFalse(invalidEntry2.isValid());

        // Invalid CellEntry exceeding height
        CellEntry invalidEntry3 = new CellEntry(2, Ex2Utils.HEIGHT);
        assertFalse(invalidEntry3.isValid());
    }

    @Test
    void testGetX() {
        // Check the X coordinate
        CellEntry cellEntry = new CellEntry(5, 10);
        assertEquals(5, cellEntry.getX());

        CellEntry cellEntry2 = new CellEntry(0, 0);
        assertEquals(0, cellEntry2.getX());
    }

    @Test
    void testGetY() {
        // Check the Y coordinate
        CellEntry cellEntry = new CellEntry(5, 10);
        assertEquals(10, cellEntry.getY());

        CellEntry cellEntry2 = new CellEntry(0, 0);
        assertEquals(0, cellEntry2.getY());
    }

    @Test
    void testToString() {
        // Check the conversion to spreadsheet-style coordinates
        CellEntry cellEntry1 = new CellEntry(0, 5);
        assertEquals("A5", cellEntry1.toString());

        CellEntry cellEntry2 = new CellEntry(1, 10);
        assertEquals("B10", cellEntry2.toString());

        CellEntry cellEntry3 = new CellEntry(25, 100);
        assertEquals("Z100", cellEntry3.toString());

        // Test invalid conversion (should be invalid logically)
        CellEntry invalidCellEntry = new CellEntry(-1, -5);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            invalidCellEntry.toString();
        });
    }


    @Test
    void testInvalidWidthOrHeight() {
        // Invalid width
        CellEntry cellEntry1 = new CellEntry(Ex2Utils.WIDTH, 0);
        assertFalse(cellEntry1.isValid());

        // Invalid height
        CellEntry cellEntry2 = new CellEntry(0, Ex2Utils.HEIGHT);
        assertFalse(cellEntry2.isValid());
    }
}
