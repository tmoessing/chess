package chess.movement;

public class CheckBounds {
    public CheckBounds() {}

    public boolean verify_out_of_bounds(int y_row, int x_row) {
        return y_row <= 0 || x_row <= 0 || y_row >= 9 || x_row >= 9;
    }
}
