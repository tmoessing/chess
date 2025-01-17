package chess.movement;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class DiagonalDirection extends DirectionCalculator {

    public DiagonalDirection(Collection<ChessMove> moveCollection, ChessPosition startPosition, int moveLimit) {
        this.moveCollection = moveCollection;
        this.startPosition = startPosition;
        this.moveLimit = moveLimit;
    }

    public void calculate_all_diagonal_moves() {
        this.calculate_diagonal_moves(1, 1);
        this.calculate_diagonal_moves(-1, -1);
        this.calculate_diagonal_moves(1, -1);
        this.calculate_diagonal_moves(-1, 1);
    }

    public void calculate_diagonal_moves(int y_row_increment, int x_row_increment) {
        super.restart_loop();
        while (in_bounds && moveCounter < moveLimit) {
            y_row += y_row_increment;
            x_col += x_row_increment;
            super.processes_on_coordinate(y_row, x_col);
        }
    }
}