package assignments.ex2;

public class Ex2SheetTest {

    public static void main(String[] args) {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);  // Création d'une feuille de calcul de 3x3

        System.out.println("=== Testing basic cell values ===");
        // Test 1: Remplir les cellules avec des valeurs de base
        sheet.set(0, 0, "123");
        sheet.set(1, 1, "456");
        sheet.set(2, 2, "Hello");

        System.out.println("Value at (0, 0): " + sheet.value(0, 0)); // Expected: 123
        System.out.println("Value at (1, 1): " + sheet.value(1, 1)); // Expected: 456
        System.out.println("Value at (2, 2): " + sheet.value(2, 2)); // Expected: Hello

        System.out.println("\n=== Testing formulas ===");
        // Test 2: Formule simple
        sheet.set(0, 1, "=123+456");
        System.out.println("Value at (0, 1): " + sheet.value(0, 1)); // Expected: 579

        sheet.set(1, 2, "=A0+B0");
        sheet.set(0, 0, "5");
        sheet.set(1, 0, "7");
        System.out.println("Value at (1, 2): " + sheet.value(1, 2)); // Expected: 12

        System.out.println("\n=== Testing invalid formulas ===");
        // Test 3: Formule invalide
        sheet.set(0, 2, "=2++2");
        System.out.println("Value at (0, 2): " + sheet.value(0, 2)); // Expected: ERR_FORM

        System.out.println("\n=== Testing circular references ===");
        // Test 4: Références circulaires
        sheet.set(0, 0, "=B0");
        sheet.set(1, 0, "=A0");
        System.out.println("Value at (0, 0): " + sheet.value(0, 0)); // Expected: ERR_CYCLE
        System.out.println("Value at (1, 0): " + sheet.value(1, 0)); // Expected: ERR_CYCLE

        System.out.println("\n=== Testing complex formulas with parentheses ===");
        // Test 5: Formules complexes avec parenthèses
        sheet.set(0, 0, "10");
        sheet.set(1, 0, "=A0*(3+2)");
        System.out.println("Value at (1, 0): " + sheet.value(1, 0)); // Expected: 50

        System.out.println("\n=== Testing division by zero ===");
        // Test 6: Division par zéro
        sheet.set(1, 0, "=A0/0");
        System.out.println("Value at (1, 0): " + sheet.value(1, 0)); // Expected: ERR_FORM

        System.out.println("\n=== Testing large numbers ===");
        // Test 7: Grands nombres
        sheet.set(0, 0, "999999999");
        sheet.set(1, 0, "=A0*2");
        System.out.println("Value at (1, 0): " + sheet.value(1, 0)); // Expected: 1999999998

        System.out.println("\n=== Testing save/load ===");
        // Test 8: Sauvegarde et chargement
        try {
            sheet.save("test.csv");
            Ex2Sheet loadedSheet = new Ex2Sheet();
            loadedSheet.load("test.csv");

            System.out.println("Loaded value at (0, 0): " + loadedSheet.value(0, 0)); // Expected: 999999999
            System.out.println("Loaded value at (1, 0): " + loadedSheet.value(1, 0)); // Expected: 1999999998
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
