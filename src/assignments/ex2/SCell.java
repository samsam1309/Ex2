package assignments.ex2;

public class SCell implements Cell {
    private String line; // Raw content of the cell.
    private int type;    // Represents the type of the cell.
    private int order;   // Represents the evaluation order.

    /**
     * Constructor for SCell.
     * Initializes the cell with the given string and determines its type.
     * @param s Initial content of the cell.
     */
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

    private int parseType(String s) {
        if (s == null || s.isEmpty()) {
            return Ex2Utils.TEXT;
        }
        if (isForm(s)) {
            return Ex2Utils.FORM;
        }
        if (isNumber(s)) {
            return Ex2Utils.NUMBER;
        }
        return Ex2Utils.TEXT;
    }

    public boolean isNumber(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isForm(String text) {
        return text != null && text.startsWith("=") && validateFormula(text.substring(1));
    }

    private boolean validateFormula(String formula) {
        int balance = 0;
        for (char c : formula.toCharArray()) {
            if (c == '(') balance++;
            else if (c == ')') balance--;
            if (balance < 0) return false; // Closing parenthesis before an opening one.
        }
        return balance == 0 && !formula.matches(".*[+\\-*/]{2,}.*");
    }

    public Double computeForm(String form) {
        if (!isForm(form)) {
            throw new IllegalArgumentException("Invalid formula: " + form);
        }
        String expression = form.substring(1).trim();
        return evaluateMathExpression(expression);
    }

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
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

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
