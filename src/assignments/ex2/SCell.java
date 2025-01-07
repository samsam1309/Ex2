package assignments.ex2;

public class SCell implements Cell {
    private String line; // Stores the raw content of the cell (e.g., "abc", "4.2", "=A1+B2").
    private int type; // Represents the type of the cell (TEXT, NUMBER, FORM, etc.).
    private int order; // Represents the evaluation order of the cell (used for dependencies).

    /**
     * Constructor for SCell.
     * Initializes the cell with the given string and determines its type.
     * @param s the initial content of the cell.
     */
    public SCell(String s) {
        setData(s); // Initializes the data and determines the type.
        this.order = 0; // Default order for the cell.
    }

    /**
     * Returns the evaluation order of the cell.
     * If the cell has dependencies, this value will be updated later.
     * @return the order of the cell.
     */
    @Override
    public int getOrder() {
        return order;
    }

    /**
     * Sets the evaluation order of the cell.
     * @param t the new order of the cell.
     */
    @Override
    public void setOrder(int t) {
        this.order = t;
    }

    /**
     * Updates the content of the cell.
     * Also determines the type of the cell based on the content.
     * @param s the new content of the cell.
     */
    @Override
    public void setData(String s) {
        this.line = s;
        this.type = parseType(s); // Automatically determine the type.
    }

    /**
     * Returns the raw content of the cell.
     * @return the raw string data of the cell.
     */
    @Override
    public String getData() {
        return line;
    }

    /**
     * Returns the type of the cell.
     * The type is determined when the cell content is set.
     * @return the type of the cell (e.g., TEXT, NUMBER, FORM).
     */
    @Override
    public int getType() {
        return type;
    }

    /**
     * Manually sets the type of the cell.
     * This is useful in cases where an error is detected.
     * @param t the new type of the cell.
     */
    @Override
    public void setType(int t) {
        this.type = t;
    }

    /**
     * Converts the cell to a string representation.
     * This method is primarily used for displaying the cell's content.
     * @return the string representation of the cell's data.
     */
    @Override
    public String toString() {
        return getData();
    }

    /**
     * Determines the type of the cell based on its content.
     * - If the content is a number (e.g., "4.2"), it is a NUMBER.
     * - If the content starts with '=', it is a FORM.
     * - If the content is plain text (e.g., "abc"), it is TEXT.
     * @param s the content of the cell.
     * @return the type of the cell (as defined in Ex2Utils).
     */
    private int parseType(String s) {
        if (s == null || s.isEmpty()) {
            return Ex2Utils.TEXT; // Default to TEXT if the content is empty.
        }
        if (s.startsWith("=")) {
            // The content starts with '=', so it is a formula.
            return Ex2Utils.FORM;
        }
        try {
            // Attempt to parse the content as a double.
            Double.parseDouble(s);
            return Ex2Utils.NUMBER; // If successful, it's a NUMBER.
        } catch (NumberFormatException e) {
            // If parsing fails, it's a TEXT.
            return Ex2Utils.TEXT;
        }
    }
}
