package assignments.ex2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SCellTest {

    @Test
    void testIsNumber() {
        SCell cell = new SCell("123");
        assertTrue(cell.isNumber("123")); // Positive integer
        assertTrue(cell.isNumber("-123.45")); // Negative decimal
        assertFalse(cell.isNumber("abc")); // Not a number
        assertFalse(cell.isNumber("123abc")); // Mixed string
    }

    @Test
    void testIsText() {
        SCell cell = new SCell("Hello");
        assertTrue(cell.isText("Hello")); // Simple text
        assertFalse(cell.isText("123")); // A number
        assertFalse(cell.isText("=1+2")); // A formula
    }

    @Test
    void testIsForm() {
        SCell cell = new SCell("=1+2");
        assertTrue(cell.isForm("=1+2")); // Valid formula
        assertTrue(cell.isForm("=(1+2)*3")); // Valid formula with parentheses
        assertFalse(cell.isForm("1+2")); // Missing '='
        assertFalse(cell.isForm("=1++2")); // Invalid formula
    }

    @Test
    void testComputeForm() {
        SCell cell = new SCell("=1+2*3");
        assertEquals(7.0, cell.computeForm("=1+2*3")); // Correct formula result
        assertEquals(9.0, cell.computeForm("=(1+2)*3")); // Parentheses precedence
        assertThrows(IllegalArgumentException.class, () -> cell.computeForm("=1++2")); // Invalid formula
    }

    @Test
    void testToString() {
        SCell cell = new SCell("Hello");
        assertEquals("Hello", cell.toString()); // Ensures toString returns the raw data
    }

    @Test
    void testGetType() {
        SCell numberCell = new SCell("123");
        assertEquals(Ex2Utils.NUMBER, numberCell.getType()); // Type should be NUMBER

        SCell textCell = new SCell("Hello");
        assertEquals(Ex2Utils.TEXT, textCell.getType()); // Type should be TEXT

        SCell formCell = new SCell("=1+2");
        assertEquals(Ex2Utils.FORM, formCell.getType()); // Type should be FORM

        SCell invalidFormCell = new SCell("=1++2");
        assertEquals(Ex2Utils.ERR_FORM_FORMAT, invalidFormCell.getType()); // Invalid formula type
    }

    @Test
    void testSetData() {
        SCell cell = new SCell("123");
        cell.setData("Hello");
        assertEquals("Hello", cell.getData()); // Ensure data is updated
        assertEquals(Ex2Utils.TEXT, cell.getType()); // Ensure type is updated correctly
    }

    @Test
    void testGetData() {
        SCell cell = new SCell("Test");
        assertEquals("Test", cell.getData()); // Ensures the raw content is retrieved
    }

    @Test
    void testValidateFormula() {
        SCell cell = new SCell("=1+2");

        // Valid formulas
        assertTrue(cell.isForm("=1+2"));
        assertTrue(cell.isForm("=(1+2)*3"));
        assertTrue(cell.isForm("=A1+B1"));

        // Invalid formulas
        assertFalse(cell.isForm("=1++2"));
        assertFalse(cell.isForm("=(1+2"));
        assertFalse(cell.isForm("=1+)2"));
    }

    @Test
    void testParseType() {
        SCell numberCell = new SCell("123");
        assertEquals(Ex2Utils.NUMBER, numberCell.getType()); // Should detect as NUMBER

        SCell textCell = new SCell("Hello");
        assertEquals(Ex2Utils.TEXT, textCell.getType()); // Should detect as TEXT

        SCell formulaCell = new SCell("=1+2");
        assertEquals(Ex2Utils.FORM, formulaCell.getType()); // Should detect as FORM

        SCell invalidFormulaCell = new SCell("=1++2");
        assertEquals(Ex2Utils.ERR_FORM_FORMAT, invalidFormulaCell.getType()); // Should detect as invalid FORM
    }

    @Test
    void testOrderManagement() {
        SCell cell = new SCell("123");
        assertEquals(0, cell.getOrder()); // Default order is 0

        cell.setOrder(5);
        assertEquals(5, cell.getOrder()); // Order should be updated
    }

    @Test
    void testEmptyAndNullStrings() {
        SCell emptyCell = new SCell("");
        assertEquals(Ex2Utils.TEXT, emptyCell.getType()); // Empty string should be TEXT

        SCell nullCell = new SCell(null);
        assertEquals(Ex2Utils.TEXT, nullCell.getType()); // Null string should be TEXT
    }

    @Test
    void testEdgeCases() {
        // Large numbers
        SCell largeNumberCell = new SCell("9999999999");
        assertTrue(largeNumberCell.isNumber("9999999999"));
        assertEquals(Ex2Utils.NUMBER, largeNumberCell.getType());

        // Negative numbers
        SCell negativeNumberCell = new SCell("-123.45");
        assertTrue(negativeNumberCell.isNumber("-123.45"));
        assertEquals(Ex2Utils.NUMBER, negativeNumberCell.getType());

        // Zero
        SCell zeroCell = new SCell("0");
        assertTrue(zeroCell.isNumber("0"));
        assertEquals(Ex2Utils.NUMBER, zeroCell.getType());
    }
}
