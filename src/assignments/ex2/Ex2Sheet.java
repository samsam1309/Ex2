package assignments.ex2;

import java.io.*;
import java.util.*;

public class Ex2Sheet implements Sheet {
    private Cell[][] table; // The grid of cells
    private Set<String> evaluatedCells = new HashSet<>(); // Tracks already evaluated cells

    // Constructor to initialize the sheet with given dimensions
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL); // Initialize cells as empty
            }
        }
        eval(); // Evaluate all formulas on initialization
    }

    // Default constructor with predefined dimensions
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        if (!isIn(x, y)) return Ex2Utils.EMPTY_CELL; // Return empty if out of bounds

        Cell c = get(x, y);
        if (c.getType() == Ex2Utils.FORM) {
            return eval(x, y); // Evaluate formula
        } else if (c.getType() == Ex2Utils.ERR_FORM_FORMAT) {
            return Ex2Utils.ERR_FORM; // Invalid formula error
        } else if (c.getType() == Ex2Utils.ERR_CYCLE_FORM) {
            return Ex2Utils.ERR_CYCLE; // Circular reference error
        }
        return c.toString(); // Return raw cell value
    }

    @Override
    public Cell get(int x, int y) {
        if (isIn(x, y)) {
            return table[x][y];
        }
        return null; // Return null if out of bounds
    }

    @Override
    public Cell get(String cords) {
        if (cords == null || cords.length() < 2) return null;

        // Convert column letter to uppercase for case-insensitivity
        cords = cords.toUpperCase();

        char col = cords.charAt(0);
        int row;
        try {
            row = Integer.parseInt(cords.substring(1)); // Extract the row index
        } catch (NumberFormatException e) {
            return null; // Invalid format
        }

        int x = col - 'A'; // Convert column letter to index
        int y = row;

        return get(x, y); // Call the numeric coordinates version
    }

    @Override
    public int width() {
        return table.length; // Return the number of columns
    }

    @Override
    public int height() {
        return table[0].length; // Return the number of rows
    }

    @Override
    public void set(int x, int y, String s) {
        if (isIn(x, y)) {
            table[x][y] = new SCell(s); // Update the cell with the new value
        }
    }

    @Override
    public void eval() {
        evaluatedCells.clear(); // Reset the set of evaluated cells

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (table[x][y].getType() == Ex2Utils.FORM) {
                    eval(x, y); // Evaluate formulas
                }
            }
        }
    }

    @Override
    public boolean isIn(int xx, int yy) {
        // Check if coordinates are within the sheet bounds
        return xx >= 0 && yy >= 0 && xx < width() && yy < height();
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (table[x][y].getType() == Ex2Utils.FORM) {
                    ans[x][y] = calculateDepth(x, y, new HashSet<>()); // Calculate formula depth
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
                set(x, y, cells[x]); // Populate cells with loaded data
            }
            y++;
        }
        reader.close();
        eval(); // Re-evaluate formulas after loading
    }

    @Override
    public void save(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                writer.write(value(x, y)); // Write cell value
                if (x < width() - 1) writer.write(","); // Separate values with commas
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
                String result = evaluateFormula(c.getData(), new HashSet<>());
                return result;
            } catch (ArithmeticException e) {
                c.setType(Ex2Utils.ERR_FORM_FORMAT);
                return Ex2Utils.ERR_FORM; // Division by zero error
            } catch (IllegalArgumentException e) {
                c.setType(Ex2Utils.ERR_CYCLE_FORM);
                return Ex2Utils.ERR_CYCLE; // Circular reference error
            } catch (Exception e) {
                c.setType(Ex2Utils.ERR_FORM_FORMAT);
                return Ex2Utils.ERR_FORM; // Other formula errors
            }
        }
        return c.toString();
    }

    private String evaluateFormula(String formula, Set<String> visitedCells) {
        if (visitedCells.contains(formula)) {
            return Ex2Utils.ERR_CYCLE; // Circular reference detected
        }
        visitedCells.add(formula);

        if (formula == null || !formula.startsWith("=")) {
            return Ex2Utils.ERR_FORM; // Invalid formula
        }

        String expression = formula.substring(1).trim();
        if (!validateFormula(expression)) {
            return Ex2Utils.ERR_FORM;
        }

        try {
            if (expression.matches(".*[()]+.*")) {
                return String.valueOf(evaluateExpressionWithParentheses(expression));
            }

            String[] tokens = expression.split("(?=[+\\-*/])|(?<=[+\\-*/])");
            Stack<Double> values = new Stack<>();
            Stack<Character> operators = new Stack<>();

            for (String token : tokens) {
                token = token.trim();
                if ("+-*/".contains(token)) {
                    operators.push(token.charAt(0)); // Push operator
                } else if (token.matches("^[A-Z]+[0-9]+$")) {
                    values.push(getValueFromReference(token)); // Handle cell reference
                } else {
                    values.push(Double.parseDouble(token)); // Parse number
                }
            }

            while (!operators.isEmpty()) {
                double b = values.pop();
                double a = values.pop();
                char op = operators.pop();
                values.push(applyOperator(op, a, b)); // Apply operator
            }
            return String.valueOf(values.pop());
        } catch (Exception e) {
            return Ex2Utils.ERR_FORM; // Formula evaluation failed
        }
    }

    private boolean validateFormula(String formula) {
        int balance = 0;
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false;
            if ("+-*/".indexOf(c) != -1) {
                if (i == 0 || i == formula.length() - 1) return false; // Invalid operator position
                if ("+-*/".indexOf(formula.charAt(i + 1)) != -1) return false; // Consecutive operators
            }
        }
        return balance == 0;
    }

    private double evaluateExpressionWithParentheses(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm(); // Addition
                    else if (eat('-')) x -= parseTerm(); // Subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor(); // Multiplication
                    else if (eat('/')) x /= parseFactor(); // Division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // Unary plus
                if (eat('-')) return -parseFactor(); // Unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else if (ch >= 'A' && ch <= 'Z') {
                    while (ch >= 'A' && ch <= 'Z') nextChar();
                    x = getValueFromReference(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }

    private double applyOperator(char op, double a, double b) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new ArithmeticException("Division by zero");
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unknown operator");
        };
    }
    private double getValueFromReference(String ref) {
        // Normalize reference to uppercase for consistency
        ref = ref.toUpperCase();

        Cell cell = get(ref);
        if (cell == null) {
            throw new IllegalArgumentException("Invalid reference: " + ref);
        }

        // If the cell contains a formula, evaluate it
        if (cell.getType() == Ex2Utils.FORM) {
            String value = eval(ref.charAt(0) - 'A', Integer.parseInt(ref.substring(1)));
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Non-numeric reference: " + ref);
            }
        }

        // If the cell contains a number, return its value
        try {
            return Double.parseDouble(cell.getData());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Non-numeric reference: " + ref);
        }
    }


    private int calculateDepth(int x, int y, Set<String> visited) {
        String cellRef = (char) ('A' + x) + String.valueOf(y);
        if (visited.contains(cellRef)) return Ex2Utils.ERR;
        visited.add(cellRef);

        Cell c = get(x, y);
        if (c.getType() != Ex2Utils.FORM) return 0;

        String formula = c.getData().substring(1).trim();
        int maxDepth = 0;

        for (String token : formula.split("[^A-Za-z0-9]")) {
            if (token.matches("[A-Z]+[0-9]+")) {
                Cell refCell = get(token);
                if (refCell != null) {
                    maxDepth = Math.max(maxDepth, calculateDepth(token.charAt(0) - 'A', Integer.parseInt(token.substring(1)), visited));
                }
            }
        }
        return 1 + maxDepth;
    }
}
