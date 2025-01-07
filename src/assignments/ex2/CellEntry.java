package assignments.ex2;

public class CellEntry implements Index2D {

    private int x, y;

    // Constructeur qui permet d'initialiser x et y
    public CellEntry(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isValid() {
        // Exemple simple : retour vrai si x et y sont dans des limites acceptables
        return x >= 0 && x < Ex2Utils.WIDTH && y >= 0 && y < Ex2Utils.HEIGHT;
    }

    @Override
    public int getX() {
        return this.x;  // Retourne l'index x
    }

    @Override
    public int getY() {
        return this.y;  // Retourne l'index y
    }

    @Override
    public String toString() {
        // Retourne la référence de la cellule (ex: "B3")
        return Ex2Utils.ABC[x] + (Ex2Utils.HEIGHT - y);  // Le format de cellule "B3"
    }
}
