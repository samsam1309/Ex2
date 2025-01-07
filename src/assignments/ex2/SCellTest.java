package assignments.ex2;

public class SCellTest {
    public static void main(String[] args) {
        // Test 1: Cell with a number
        SCell cell1 = new SCell("123");
        System.out.println("Data: " + cell1.getData()); // Should print: 123
        System.out.println("Type: " + cell1.getType()); // Should print: Ex2Utils.NUMBER

        // Test 2: Cell with a formula
        SCell cell2 = new SCell("=A1+B2");
        System.out.println("Data: " + cell2.getData()); // Should print: =A1+B2
        System.out.println("Type: " + cell2.getType()); // Should print: Ex2Utils.FORM

        // Test 3: Cell with text
        SCell cell3 = new SCell("Hello");
        System.out.println("Data: " + cell3.getData()); // Should print: Hello
        System.out.println("Type: " + cell3.getType()); // Should print: Ex2Utils.TEXT

        // Test 4: Empty cell
        SCell cell4 = new SCell("");
        System.out.println("Data: " + cell4.getData()); // Should print: (empty string)
        System.out.println("Type: " + cell4.getType()); // Should print: Ex2Utils.TEXT
    }
}
