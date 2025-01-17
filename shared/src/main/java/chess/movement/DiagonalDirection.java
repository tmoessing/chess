package chess.movement;

import chess.*;
import java.util.*;

public class DiagonalDirection extends DirectionCalculator {

    public DiagonalDirection(ChessBoard chessBoard, ChessPiece chessPiece, Collection<ChessMove> moveCollection, ChessPosition startPosition, int moveLimit) {
        this.chessBoard = chessBoard;
        this.chessPiece = chessPiece;
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
        while (in_bounds && not_blocked && moveCounter < moveLimit) {
            y_row += y_row_increment;
            x_col += x_row_increment;
            super.processes_on_coordinate(y_row, x_col);
        }
    }
}