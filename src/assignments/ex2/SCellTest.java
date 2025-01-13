package assignments.ex2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SCellTest {

    @Test
    public void testSetDataAndGetData() {
        SCell cell = new SCell("Hello");
        assertEquals("Hello", cell.getData(), "The data should be 'Hello'");
        cell.setData("42");
        assertEquals("42", cell.getData(), "The data should be '42'");
    }

    @Test
    public void testParseTypeText() {
        SCell cell = new SCell("Hello");
        assertEquals(Ex2Utils.TEXT, cell.getType(), "The type should be TEXT for plain text");
    }

    @Test
    public void testParseTypeNumber() {
        SCell cell = new SCell("42");
        assertEquals(Ex2Utils.NUMBER, cell.getType(), "The type should be NUMBER for a valid number");
    }

    @Test
    public void testParseTypeFormula() {
        SCell cell = new SCell("=2+3");
        assertEquals(Ex2Utils.FORM, cell.getType(), "The type should be FORM for a valid formula");
    }


    @Test
    public void testParseTypeInvalidFormula() {
        SCell cell = new SCell("=2++2");
        assertEquals(Ex2Utils.ERR_FORM_FORMAT, cell.getType(), "The type should be ERR_FORM_FORMAT for an invalid formula");

        cell.setData("=2+");
        assertEquals(Ex2Utils.ERR_FORM_FORMAT, cell.getType(), "The type should be ERR_FORM_FORMAT for a formula ending with an operator");

        cell.setData("=2*(3+4");
        assertEquals(Ex2Utils.ERR_FORM_FORMAT, cell.getType(), "The type should be ERR_FORM_FORMAT for a formula with unbalanced parentheses");
    }



    @Test
    public void testIsNumber() {
        SCell cell = new SCell("42");
        assertTrue(cell.isNumber("42"), "42 should be recognized as a valid number");
        assertFalse(cell.isNumber("Hello"), "Hello should not be recognized as a valid number");
    }

    @Test
    public void testIsForm() {
        SCell cell = new SCell("=2+3");
        assertTrue(cell.isForm("=2+3"), "=2+3 should be recognized as a valid formula");
        assertFalse(cell.isForm("2+3"), "2+3 should not be recognized as a valid formula");
        assertFalse(cell.isForm("=2++3"), "=2++3 should not be recognized as a valid formula");
    }

    @Test
    public void testValidateFormula() {
        SCell cell = new SCell("=2+3");
        assertTrue(cell.isForm("=2+3"), "A valid formula should pass validation");
        assertFalse(cell.isForm("=2++3"), "An invalid formula should fail validation");
        assertFalse(cell.isForm("=2+"), "A formula with a trailing operator should fail validation");
        assertFalse(cell.isForm("=2*(3+4"), "A formula with unbalanced parentheses should fail validation");
    }

    @Test
    public void testComputeValidFormulas() {
        SCell cell = new SCell("=2+3");
        assertEquals(5.0, cell.computeForm("=2+3"), "The formula =2+3 should evaluate to 5.0");
        cell.setData("=3*(2+4)");
        assertEquals(18.0, cell.computeForm("=3*(2+4)"), "The formula =3*(2+4) should evaluate to 18.0");
    }

    @Test
    public void testComputeInvalidFormula() {
        SCell cell = new SCell("=2++2");
        assertThrows(IllegalArgumentException.class, () -> cell.computeForm("=2++2"), "Invalid formulas should throw an exception");
    }

    @Test
    public void testToString() {
        SCell cell = new SCell("Hello");
        assertEquals("Hello", cell.toString(), "The toString method should return the cell data");
    }
}
