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

        // Vérification des valeurs
        assertEquals("123", sheet.value(0, 0));
        assertEquals("456", sheet.value(1, 1));
        assertEquals("Hello", sheet.value(2, 2));
    }

    @Test
    void testFormulas() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Formules simples
        sheet.set(0, 1, "=123+456");
        assertEquals("579.0", sheet.value(0, 1));

        // Références croisées
        sheet.set(0, 0, "5");
        sheet.set(1, 0, "7");
        sheet.set(1, 2, "=A0+B0");
        assertEquals("12.0", sheet.value(1, 2));
    }

    @Test
    void testInvalidFormulas() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Formules invalides
        sheet.set(0, 0, "=2++2");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(0, 0));

        sheet.set(1, 1, "=(2+3");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(1, 1));
    }

    @Test
    void testDepthCalculation() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Dépendances simples
        sheet.set(0, 0, "5");
        sheet.set(1, 0, "=A0+1");
        sheet.set(2, 0, "=B0+2");

        int[][] depth = sheet.depth();
        assertEquals(0, depth[0][0]);
        assertEquals(1, depth[1][0]);
        assertEquals(2, depth[2][0]);
    }

    @Test
    void testSaveAndLoad() throws Exception {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        // Sauvegarde et chargement
        sheet.set(0, 0, "123");
        sheet.set(1, 1, "=A0*2");
        sheet.set(2, 2, "Hello");
        sheet.save("test.csv");

        Ex2Sheet loadedSheet = new Ex2Sheet();
        loadedSheet.load("test.csv");
        assertEquals("123", loadedSheet.value(0, 0));
        assertEquals("246.0", loadedSheet.value(1, 1));
        assertEquals("Hello", loadedSheet.value(2, 2));
    }
}
