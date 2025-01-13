package assignments.ex2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SCellTest {

    @Test
    void testIsNumber() {
        SCell cell = new SCell("123");
        assertTrue(cell.isNumber("123"));
        assertTrue(cell.isNumber("-123.45"));
        assertFalse(cell.isNumber("abc"));
        assertFalse(cell.isNumber("123abc"));
    }

    @Test
    void testIsText() {
        SCell cell = new SCell("Hello");
        assertTrue(cell.isText("Hello"));
        assertFalse(cell.isText("123"));
        assertFalse(cell.isText("=1+2"));
    }

    @Test
    void testIsForm() {
        SCell cell = new SCell("=1+2");
        assertTrue(cell.isForm("=1+2"));
        assertTrue(cell.isForm("=(1+2)*3"));
        assertFalse(cell.isForm("1+2"));
        assertFalse(cell.isForm("=1++2"));
    }

    @Test
    void testComputeForm() {
        SCell cell = new SCell("=1+2*3");
        assertEquals(7.0, cell.computeForm("=1+2*3"));
        assertEquals(9.0, cell.computeForm("=(1+2)*3"));
        assertThrows(IllegalArgumentException.class, () -> cell.computeForm("=1++2"));
    }
}
