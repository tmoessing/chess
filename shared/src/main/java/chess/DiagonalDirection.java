package chess;

import java.util.ArrayList;
import java.security.KeyStore;

public class DiagonalDirection {
    private int start_row;
    private int start_col;
    private ArrayList<ChessPosition> possible_postions;

    public DiagonalDirection(ChessPosition start_position) {
        this.start_row = start_position.getRow();
        this.start_col = start_position.getColumn();
    }

    public void calculate_upper_right_postions() {
        boolean in_bounds = true;
        int x = this.start_row;
        int y = this.start_col;

        while (in_bounds) {
            x = x + 1;
            y = y + 1;

            if (new CheckBounds().verify_in_bounds()) {
                in_bounds = false;
            }
            else {
                ChessPosition possible_position = new ChessPosition(x, y);
                possible_postions.add(possible_position);
            }
        }
    }

}
