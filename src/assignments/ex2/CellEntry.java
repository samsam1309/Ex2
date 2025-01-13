package assignments.ex2;

public class CellEntry implements Index2D {

    private int x, y;

    // Constructor to initialize x and y coordinates
    public CellEntry(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isValid() {
        // Simple example: returns true if x and y are within valid limits
        return x >= 0 && x < Ex2Utils.WIDTH && y >= 0 && y < Ex2Utils.HEIGHT;
    }

    @Override
    public int getX() {
        return this.x;  // Returns the x index
    }

    @Override
    public int getY() {
        return this.y;  // Returns the y index
    }

    @Override
    public String toString() {
        // Converts the cell coordinates to a spreadsheet-style format, e.g., "A1" instead of "A16"
        return Ex2Utils.ABC[x] + y;
    }
}
