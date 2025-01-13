package assignments.ex2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Ex2SheetTest {

    @Test
    void testBasicCellValues() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Ajout de données dans les cellules
        sheet.set(0, 0, "123");
        sheet.set(1, 1, "456");
        sheet.set(2, 2, "Hello");

        // Vérification avec des assertions
        assertEquals("123", sheet.value(0, 0));
        assertEquals("456", sheet.value(1, 1));
        assertEquals("Hello", sheet.value(2, 2));
    }

    @Test
    void testFormulas() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Formule simple
        sheet.set(0, 1, "=123+456");
        assertEquals("579.0", sheet.value(0, 1));

        // Formule avec référence
        sheet.set(0, 0, "5");
        sheet.set(1, 0, "7");
        sheet.set(1, 2, "=A0+B0");
        assertEquals("12.0", sheet.value(1, 2));
    }

    @Test
    void testInvalidFormulas() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Formule invalide (double opérateur)
        sheet.set(0, 0, "=2++2");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(0, 0));

        // Formule avec parenthèse mal équilibrée
        sheet.set(1, 1, "=(2+3");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(1, 1));

        // Formule invalide avec opérateur en fin
        sheet.set(2, 2, "=2+");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(2, 2));
    }




    @Test
    void testDivisionByZero() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Division par zéro
        sheet.set(0, 0, "=10/0");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(0, 0));
    }


    @Test
    void testEmptyCells() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Cellule vide
        assertEquals(Ex2Utils.EMPTY_CELL, sheet.value(0, 0));
    }

    @Test
    void testSaveAndLoad() throws Exception {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Ajouter des données
        sheet.set(0, 0, "123");
        sheet.set(1, 1, "=A0*2");
        sheet.set(2, 2, "Hello");

        // Sauvegarde et chargement
        sheet.save("test.csv");
        Ex2Sheet loadedSheet = new Ex2Sheet();
        loadedSheet.load("test.csv");

        // Vérifier les valeurs après chargement
        assertEquals("123", loadedSheet.value(0, 0));
        assertEquals("246.0", loadedSheet.value(1, 1));
        assertEquals("Hello", loadedSheet.value(2, 2));
    }

}
