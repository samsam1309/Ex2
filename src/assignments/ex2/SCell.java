package assignments.ex2;

public class SCell implements Cell {
    private String line; // Raw content of the cell
    private int type;    // Type of the cell (Ex2Utils.TEXT, Ex2Utils.NUMBER, Ex2Utils.FORM, etc.)
    private int order;   // Evaluation order of the cell

    // Constructor
    public SCell(String s) {
        setData(s);
        this.order = 0;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int t) {
        this.order = t;
    }

    @Override
    public void setData(String s) {
        this.line = s;
        this.type = parseType(s);
    }

    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        this.type = t;
    }

    @Override
    public String toString() {
        return getData();
    }

    /**
     * Determines the type of the cell (TEXT, NUMBER, FORM, or error).
     */
    private int parseType(String s) {
        if (s == null || s.isEmpty()) {
            return Ex2Utils.TEXT; // An empty or null string is considered text
        }
        if (s.startsWith("=")) {
            // Check if it is a valid or invalid formula
            if (validateFormula(s.substring(1))) {
                return Ex2Utils.FORM; // Valid formula
            } else {
                return Ex2Utils.ERR_FORM_FORMAT; // Invalid formula
            }
        }
        if (isNumber(s)) {
            return Ex2Utils.NUMBER; // Valid number
        }
        return Ex2Utils.TEXT; // Otherwise, it is text
    }

    /**
     * Checks if a string is a valid number.
     */
    public boolean isNumber(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string is text.
     */
    public boolean isText(String text) {
        return !isNumber(text) && !isForm(text);
    }

    /**
     * Checks if a string is a valid formula.
     */
    public boolean isForm(String text) {
        return text != null && text.startsWith("=") && validateFormula(text.substring(1));
    }

    /**
     * Validates the syntax of a formula.
     */
    private boolean validateFormula(String formula) {
        int balance = 0; // Parentheses balance

        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);

            // Check the balance of parentheses
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false; // Closing parenthesis before opening

            // Check for consecutive or misplaced operators
            if ("+-*/".indexOf(c) != -1) {
                if (i == 0 || i == formula.length() - 1) return false; // Operator at the start or end
                if (i > 0 && "+-*/".indexOf(formula.charAt(i - 1)) != -1) return false; // Consecutive operators
            }
        }

        return balance == 0; // All parentheses must be closed
    }

    /**
     * Calculates the result of a valid formula.
     */
    public Double computeForm(String form) {
        if (!isForm(form) || !validateFormula(form.substring(1))) {
            throw new IllegalArgumentException("Invalid formula: " + form);
        }
        String expression = form.substring(1).trim();
        return evaluateMathExpression(expression);
    }

    /**
     * Evaluates a mathematical expression.
     */
    private Double evaluateMathExpression(String expression) {
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
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}
