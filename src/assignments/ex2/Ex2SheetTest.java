package assignments.ex2;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class Ex2SheetTest {

    @Test
    void testBasicCellValues() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Set basic values in cells
        sheet.set(0, 0, "123");
        sheet.set(1, 1, "456");
        sheet.set(2, 2, "Hello");

        // Verify cell values
        assertEquals("123", sheet.value(0, 0));
        assertEquals("456", sheet.value(1, 1));
        assertEquals("Hello", sheet.value(2, 2));
    }

    @Test
    void testFormulas() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Simple formulas
        sheet.set(0, 1, "=123+456");
        assertEquals("579.0", sheet.value(0, 1));

        // Cross-referenced formulas
        sheet.set(0, 0, "5");
        sheet.set(1, 0, "7");
        sheet.set(1, 2, "=A0+B0");
        assertEquals("12.0", sheet.value(1, 2));
    }

    @Test
    void testInvalidFormulas() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Invalid formulas
        sheet.set(0, 0, "=2++2");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(0, 0));

        sheet.set(1, 1, "=(2+3");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(1, 1));
    }

    @Test
    void testEmptyCells() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Access empty cells
        assertEquals(Ex2Utils.EMPTY_CELL, sheet.value(0, 0));
        assertEquals(Ex2Utils.EMPTY_CELL, sheet.value(2, 2));
    }

    @Test
    void testDepthCalculation() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Set up dependencies
        sheet.set(0, 0, "5");
        sheet.set(1, 0, "=A0+1");
        sheet.set(2, 0, "=B0+2");

        // Verify depth calculation
        int[][] depth = sheet.depth();
        assertEquals(0, depth[0][0]);
        assertEquals(1, depth[1][0]);
        assertEquals(2, depth[2][0]);
    }

    @Test
    void testSaveAndLoad() throws Exception {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Set values and save to file
        sheet.set(0, 0, "123");
        sheet.set(1, 1, "=A0*2");
        sheet.set(2, 2, "Hello");
        File testFile = new File("test.csv");
        sheet.save(testFile.getAbsolutePath());

        // Load values back
        Ex2Sheet loadedSheet = new Ex2Sheet();
        loadedSheet.load(testFile.getAbsolutePath());
        assertEquals("123", loadedSheet.value(0, 0));
        assertEquals("246.0", loadedSheet.value(1, 1));
        assertEquals("Hello", loadedSheet.value(2, 2));

        // Cleanup
        assertTrue(testFile.delete());
    }

    @Test
    void testGetCellByReference() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Set values
        sheet.set(0, 0, "10"); // A0
        sheet.set(1, 0, "20"); // B0
        sheet.set(2, 0, "=A0+B0"); // C0 = A0 + B0

        // Verify access with numeric indices
        assertEquals("10", sheet.get(0, 0).getData());
        assertEquals("20", sheet.get(1, 0).getData());
        assertEquals("=A0+B0", sheet.get(2, 0).getData());

        // Verify access with textual references (case-insensitive)
        assertEquals("10", sheet.get("A0").getData());
        assertEquals("10", sheet.get("a0").getData());
        assertEquals("20", sheet.get("B0").getData());
        assertEquals("=A0+B0", sheet.get("c0").getData());
    }

    @Test
    void testReferencedFormulas() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Set formulas referencing other cells
        sheet.set(0, 0, "=2+2"); // A0 = 4
        assertEquals("4.0", sheet.value(0, 0));

        sheet.set(1, 0, "=A0+5"); // B0 = A0 + 5 = 9
        assertEquals("9.0", sheet.value(1, 0));
    }


    @Test
    void testEdgeCases() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Test large numbers
        sheet.set(0, 0, "9999999999");
        assertEquals("9999999999", sheet.value(0, 0));

        // Test negative numbers
        sheet.set(0, 1, "-123");
        assertEquals("-123", sheet.value(0, 1));

        // Test zero
        sheet.set(1, 0, "0");
        assertEquals("0", sheet.value(1, 0));
    }
}
