package chess.movement;

import chess.*;
import java.util.*;

public class HorizontalDirection extends DirectionCalculator {

    public HorizontalDirection(ChessBoard chessBoard, ChessPiece chessPiece, Collection<ChessMove> moveCollection, ChessPosition startPosition, int moveLimit ) {
        this.chessBoard = chessBoard;
        this.chessPiece = chessPiece;
        this.startPosition = startPosition;
        this.moveCollection = moveCollection;
        this.moveLimit = moveLimit;
    }

    public void calculate_all_horizontal_moves(){
        this.calculate_horizontal_moves(1);
        this.calculate_horizontal_moves(-1);
    }

    public void calculate_horizontal_moves(int x_col_increment) {
        super.restart_loop();
        while (in_bounds && not_blocked && moveCounter < moveLimit) {
            x_col += x_col_increment;
            super.processes_on_coordinate(y_row, x_col);
        }
    }
}
