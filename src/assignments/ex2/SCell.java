package assignments.ex2;

public class SCell implements Cell {
    private String line; // Contenu brut de la cellule
    private int type;    // Type de la cellule (Ex2Utils.TEXT, Ex2Utils.NUMBER, Ex2Utils.FORM, etc.)
    private int order;   // Ordre d'évaluation de la cellule

    // Constructeur
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
     * Détermine le type de la cellule (TEXT, NUMBER, FORM, ou erreur).
     */
    private int parseType(String s) {
        if (s == null || s.isEmpty()) {
            return Ex2Utils.TEXT; // Une chaîne vide ou nulle est considérée comme du texte
        }
        if (s.startsWith("=")) {
            // Vérifie si c'est une formule valide ou invalide
            if (validateFormula(s.substring(1))) {
                return Ex2Utils.FORM; // C'est une formule valide
            } else {
                return Ex2Utils.ERR_FORM_FORMAT; // Formule invalide
            }
        }
        if (isNumber(s)) {
            return Ex2Utils.NUMBER; // C'est un nombre valide
        }
        return Ex2Utils.TEXT; // Sinon, c'est du texte
    }



    /**
     * Vérifie si une chaîne est un nombre valide.
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
     * Vérifie si une chaîne est du texte.
     */
    public boolean isText(String text) {
        return !isNumber(text) && !isForm(text);
    }

    /**
     * Vérifie si une chaîne est une formule valide.
     */
    public boolean isForm(String text) {
        return text != null && text.startsWith("=") && validateFormula(text.substring(1));
    }

    /**
     * Valide la syntaxe d'une formule.
     */
    private boolean validateFormula(String formula) {
        int balance = 0; // Équilibre des parenthèses

        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);

            // Vérifie l'équilibre des parenthèses
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false; // Parenthèse fermante avant une ouvrante

            // Vérifie les opérateurs consécutifs ou mal placés
            if ("+-*/".indexOf(c) != -1) {
                if (i == 0 || i == formula.length() - 1) return false; // Opérateur au début ou à la fin
                if (i > 0 && "+-*/".indexOf(formula.charAt(i - 1)) != -1) return false; // Opérateurs consécutifs
            }
        }

        return balance == 0; // Toutes les parenthèses doivent être fermées
    }



    /**
     * Calcule le résultat d'une formule valide.
     */
    public Double computeForm(String form) {
        if (!isForm(form) || !validateFormula(form.substring(1))) {
            throw new IllegalArgumentException("Invalid formula: " + form);
        }
        String expression = form.substring(1).trim();
        return evaluateMathExpression(expression);
    }


    /**
     * Évalue une expression mathématique.
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
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
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
