package assignments.ex2;

import java.io.*;
import java.util.*;

public class Ex2Sheet implements Sheet {
    private Cell[][] table; // La grille des cellules
    private Set<String> evaluatedCells = new HashSet<>(); // Ensemble pour suivre les cellules déjà évaluées

    // Constructeur initialisant la feuille avec les dimensions données
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
            }
        }
        eval(); // Évalue toutes les formules dans la feuille lors de l'initialisation
    }

    // Constructeur par défaut avec des dimensions prédéfinies
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        if (!isIn(x, y)) return Ex2Utils.EMPTY_CELL;
        Cell c = get(x, y);
        if (c.getType() == Ex2Utils.FORM) {
            return eval(x, y); // Évalue la formule si nécessaire
        }
        return c.toString();
    }

    @Override
    public Cell get(int x, int y) {
        if (isIn(x, y)) {
            return table[x][y];
        }
        return null;
    }

    @Override
    public Cell get(String cords) {
        if (cords == null || cords.length() < 2) return null;

        char col = cords.charAt(0); // Extraire la colonne (par exemple 'A')
        int row;
        try {
            row = Integer.parseInt(cords.substring(1)); // Extraire la ligne
        } catch (NumberFormatException e) {
            return null;
        }

        int x = col - 'A'; // Convertir la lettre de la colonne en index
        int y = row;
        return get(x, y);
    }

    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {
        if (isIn(x, y)) {
            table[x][y] = new SCell(s); // Met à jour la cellule avec la nouvelle valeur
        }
    }

    @Override
    public void eval() {
        evaluatedCells.clear(); // Réinitialiser les cellules évaluées

        int[][] dd = depth(); // Calcule les niveaux de dépendance

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (table[x][y].getType() == Ex2Utils.FORM) {
                    eval(x, y); // Évalue chaque formule
                }
            }
        }
    }

    @Override
    public boolean isIn(int xx, int yy) {
        return xx >= 0 && yy >= 0 && xx < width() && yy < height();
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (table[x][y].getType() == Ex2Utils.FORM) {
                    ans[x][y] = calculateDepth(x, y);
                }
            }
        }
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int y = 0;
        while ((line = reader.readLine()) != null && y < height()) {
            String[] cells = line.split(",");
            for (int x = 0; x < cells.length && x < width(); x++) {
                set(x, y, cells[x]);
            }
            y++;
        }
        reader.close();
        eval(); // Réévalue les formules après le chargement
    }

    @Override
    public void save(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                writer.write(value(x, y));
                if (x < width() - 1) writer.write(",");
            }
            writer.newLine();
        }
        writer.close();
    }

    @Override
    public String eval(int x, int y) {
        if (!isIn(x, y)) return Ex2Utils.EMPTY_CELL;

        Cell c = get(x, y);
        if (c.getType() == Ex2Utils.FORM) {
            try {
                // Évaluer la formule
                return evaluateFormula(c.getData());
            } catch (ArithmeticException e) {
                c.setType(Ex2Utils.ERR_FORM_FORMAT);  // Division par zéro ou erreur dans la formule
                return Ex2Utils.ERR_FORM;  // Message d'erreur standard
            } catch (IllegalArgumentException e) {
                c.setType(Ex2Utils.ERR_CYCLE_FORM);  // Référence circulaire
                return Ex2Utils.ERR_CYCLE;  // Message d'erreur de cycle
            } catch (Exception e) {
                c.setType(Ex2Utils.ERR_FORM_FORMAT);  // Autres erreurs
                return Ex2Utils.ERR_FORM;
            }
        }
        return c.toString();
    }


    // Fonction modifiée pour évaluer une formule
    private String evaluateFormula(String formula) {
        if (formula == null || !formula.startsWith("=")) {
            return Ex2Utils.ERR_FORM; // Format invalide
        }

        String expression = formula.substring(1).trim(); // Enlève '=' et espaces inutiles
        System.out.println("Evaluating formula: " + expression);

        try {
            // Vérifie si l'expression est une expression mathématique simple
            if (expression.matches("[0-9.+\\-*/\\s]+")) {
                System.out.println("Recognized as a simple math expression");
                return String.valueOf(evaluateMathExpression(expression));  // Utilise evaluateMathExpression pour les calculs simples
            }

            System.out.println("Recognized as a formula with references");
            // Si l'expression contient des références comme A1 ou B2
            String[] parts = expression.split("\\+|\\-|\\*|\\/"); // Sépare en opérandes
            char operator = expression.contains("+") ? '+' :
                    expression.contains("-") ? '-' :
                            expression.contains("*") ? '*' : '/';

            // Résout les références (par exemple "A1" -> valeur de la cellule)
            double val1 = getValueFromReference(parts[0].trim());
            double val2 = getValueFromReference(parts[1].trim());

            // Effectue le calcul
            return switch (operator) {
                case '+' -> String.valueOf(val1 + val2);
                case '-' -> String.valueOf(val1 - val2);
                case '*' -> String.valueOf(val1 * val2);
                case '/' -> val2 != 0 ? String.valueOf(val1 / val2) : Ex2Utils.ERR_FORM;
                default -> Ex2Utils.ERR_FORM;
            };
        } catch (Exception e) {
            System.out.println("Error while evaluating: " + e.getMessage());
            return Ex2Utils.ERR_FORM; // Retourne une erreur si la formule est invalide
        }
    }

    // Fonction d'évaluation des expressions mathématiques simples
    private double evaluateMathExpression(String expression) throws Exception {
        // Remplacer les espaces pour faciliter l'évaluation
        expression = expression.replaceAll("\\s+", "");

        // Gestion des opérations simples
        if (expression.contains("+")) {
            String[] operands = expression.split("\\+");
            return Double.parseDouble(operands[0]) + Double.parseDouble(operands[1]);
        }
        if (expression.contains("-")) {
            String[] operands = expression.split("-");
            return Double.parseDouble(operands[0]) - Double.parseDouble(operands[1]);
        }
        if (expression.contains("*")) {
            String[] operands = expression.split("\\*");
            return Double.parseDouble(operands[0]) * Double.parseDouble(operands[1]);
        }
        if (expression.contains("/")) {
            String[] operands = expression.split("/");
            if (Double.parseDouble(operands[1]) == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return Double.parseDouble(operands[0]) / Double.parseDouble(operands[1]);
        }

        // Si aucune opération, retourner la valeur directe
        return Double.parseDouble(expression);
    }

    // Résout les références aux cellules comme "A1"
    private double getValueFromReference(String ref) {
        Cell cell = get(ref);
        if (cell == null || cell.getType() == Ex2Utils.ERR_FORM_FORMAT) {
            throw new IllegalArgumentException("Invalid reference: " + ref);
        }
        return Double.parseDouble(cell.getData());
    }

    // Calcul de la profondeur des dépendances pour une formule
    private int calculateDepth(int x, int y) {
        Stack<Cell> stack = new Stack<>();
        stack.push(get(x, y));
        int depth = 0;

        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            if (current.getType() == Ex2Utils.FORM) {
                // Ajout de la logique pour calculer les dépendances
                // Cette logique peut impliquer la vérification des références
                // Si la cellule dépend d'une autre cellule, il faut l'ajouter à la pile
            }
            depth++;
        }

        return depth;
    }
}
