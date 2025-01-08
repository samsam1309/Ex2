package assignments.ex2;

public class SCellTest {

    public static void main(String[] args) {
        System.out.println("=== Testing SCell ===");

        // Test 1: Cell with a number
        System.out.println("\n-- Test 1: Cell with a number --");
        SCell cell1 = new SCell("123");
        System.out.println("Data: " + cell1.getData()); // Expected: 123
        System.out.println("Type: " + cell1.getType()); // Expected: Ex2Utils.NUMBER

        // Test 2: Cell with a formula
        System.out.println("\n-- Test 2: Cell with a formula --");
        SCell cell2 = new SCell("=A1+B2");
        System.out.println("Data: " + cell2.getData()); // Expected: =A1+B2
        System.out.println("Type: " + cell2.getType()); // Expected: Ex2Utils.FORM

        // Test 3: Cell with text
        System.out.println("\n-- Test 3: Cell with text --");
        SCell cell3 = new SCell("Hello");
        System.out.println("Data: " + cell3.getData()); // Expected: Hello
        System.out.println("Type: " + cell3.getType()); // Expected: Ex2Utils.TEXT

        // Test 4: Empty cell
        System.out.println("\n-- Test 4: Empty cell --");
        SCell cell4 = new SCell("");
        System.out.println("Data: " + cell4.getData()); // Expected: (empty string)
        System.out.println("Type: " + cell4.getType()); // Expected: Ex2Utils.TEXT

        // Test 5: Cell with an invalid formula
        System.out.println("\n-- Test 5: Cell with an invalid formula --");
        SCell cell5 = new SCell("=A1++B2");
        System.out.println("Data: " + cell5.getData()); // Expected: =A1++B2
        System.out.println("Type: " + cell5.getType()); // Expected: Ex2Utils.FORM

        // Test 6: Update cell content
        System.out.println("\n-- Test 6: Updating cell content --");
        SCell cell6 = new SCell("42");
        System.out.println("Initial Data: " + cell6.getData()); // Expected: 42
        System.out.println("Initial Type: " + cell6.getType()); // Expected: Ex2Utils.NUMBER

        cell6.setData("=B1+C2");
        System.out.println("Updated Data: " + cell6.getData()); // Expected: =B1+C2
        System.out.println("Updated Type: " + cell6.getType()); // Expected: Ex2Utils.FORM

        cell6.setData("Text");
        System.out.println("Updated Data: " + cell6.getData()); // Expected: Text
        System.out.println("Updated Type: " + cell6.getType()); // Expected: Ex2Utils.TEXT

        // Test 7: Cell with a simple formula
        System.out.println("\n-- Test 7: Cell with a simple formula --");
        SCell cell7 = new SCell("=1+2*3");
        System.out.println("Data: " + cell7.getData()); // Expected: =1+2*3
        System.out.println("Type: " + cell7.getType()); // Expected: Ex2Utils.FORM

        // Test 8: Cell with a complex formula
        System.out.println("\n-- Test 8: Cell with a complex formula --");
        SCell cell8 = new SCell("=(1+2)*(3+4)");
        System.out.println("Data: " + cell8.getData()); // Expected: =(1+2)*(3+4)
        System.out.println("Type: " + cell8.getType()); // Expected: Ex2Utils.FORM

        // Test 9: Edge cases
        System.out.println("\n-- Test 9: Edge cases --");
        SCell cell9 = new SCell("=0/0"); // Division by zero
        System.out.println("Data: " + cell9.getData()); // Expected: =0/0
        System.out.println("Type: " + cell9.getType()); // Expected: Ex2Utils.FORM

        SCell cell10 = new SCell(null); // Null input
        System.out.println("Data: " + cell10.getData()); // Expected: null or ""
        System.out.println("Type: " + cell10.getType()); // Expected: Ex2Utils.TEXT

        SCell cell11 = new SCell("=A1*B2+C3"); // Formula with references
        System.out.println("Data: " + cell11.getData()); // Expected: =A1*B2+C3
        System.out.println("Type: " + cell11.getType()); // Expected: Ex2Utils.FORM
    }
}
