package chess;

public class CheckBounds {
    public CheckBounds() {}

    public boolean verify_out_of_bounds(int x, int y) {
        if (x < 0 || y < 0 || x > 7 || y > 7) {
            return true; }
        else {
            return false;
        }
    }
}
