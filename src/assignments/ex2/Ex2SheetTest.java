package assignments.ex2;

public class Ex2SheetTest {

    public static void main(String[] args) {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);  // Création d'une feuille de calcul de 3x3

        System.out.println("Testing basic cell values:");
        // Test 1: Remplir les cellules avec des valeurs de base
        sheet.set(0, 0, "123");  // Cellule (0,0) avec une valeur numérique
        sheet.set(1, 1, "456");  // Cellule (1,1) avec une valeur numérique
        sheet.set(2, 2, "Hello");  // Cellule (2,2) avec un texte

        // Affichage des valeurs pour vérifier qu'elles sont correctement définies
        System.out.println("Value at (0, 0): " + sheet.value(0, 0)); // Expected: 123
        System.out.println("Value at (1, 1): " + sheet.value(1, 1)); // Expected: 456
        System.out.println("Value at (2, 2): " + sheet.value(2, 2)); // Expected: Hello

        System.out.println("\nTesting formulas:");
        // Test 2: Formule simple
        sheet.set(0, 1, "=123+456");  // Formule simple dans la cellule (0,1)
        sheet.set(1, 2, "=A0+B0");  // Référence à d'autres cellules

        // Vérification des valeurs après évaluation des formules
        System.out.println("Value at (0, 1): " + sheet.value(0, 1)); // Expected: 579
        System.out.println("Value at (1, 2): " + sheet.value(1, 2)); // Expected: 579

        System.out.println("\nTesting invalid formulas:");
        // Test 3: Formule invalide
        sheet.set(0, 2, "=2++2");  // Formule invalide (deux signes plus)
        System.out.println("Value at (0, 2): " + sheet.value(0, 2)); // Expected: ERR_FORM

        System.out.println("\nTesting references:");
        // Test 4: Références entre cellules
        sheet.set(0, 0, "3");
        sheet.set(1, 0, "5");
        sheet.set(0, 1, "=A0+B0");  // Référence aux cellules (0,0) et (1,0)
        System.out.println("Value at (0, 1): " + sheet.value(0, 1)); // Expected: 8

        System.out.println("\nTesting save/load:");
        // Test 5: Sauvegarde et chargement de la feuille
        try {
            sheet.save("test.csv");  // Sauvegarde de la feuille dans un fichier
            Ex2Sheet loadedSheet = new Ex2Sheet();  // Création d'une nouvelle feuille pour le chargement
            loadedSheet.load("test.csv");  // Chargement du fichier sauvegardé

            // Vérification des valeurs après chargement
            System.out.println("Loaded value at (0, 0): " + loadedSheet.value(0, 0)); // Expected: 3
            System.out.println("Loaded value at (2, 2): " + loadedSheet.value(2, 2)); // Expected: Hello
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
