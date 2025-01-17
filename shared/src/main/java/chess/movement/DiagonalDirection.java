package chess.movement;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class DiagonalDirection {
    public Collection<ChessMove> moveCollection;
    private ChessPosition startPosition;
    private ChessPiece.PieceType promotionPieceType = null;
    private int moveLimit;

    // Calculate Variables
    private boolean in_bounds;
    private int y_row;
    private int x_col;
    private int moveCounter;

    public DiagonalDirection(Collection<ChessMove> moveCollection,ChessPosition startPosition, int moveLimit) {
        this.moveCollection = moveCollection;
        this.startPosition = startPosition;
        this.moveLimit = moveLimit;
    }
    public void calculate_all_diagonal_moves() {
        this.calculate_diagonal_moves(1,1);
        this.calculate_diagonal_moves(-1, -1);
        this.calculate_diagonal_moves(1, -1);
        this.calculate_diagonal_moves(-1, 1);
    }
    public void restart_loop () {
        y_row = startPosition.getRow();
        x_col = startPosition.getColumn();
        in_bounds = true;
        moveCounter = 0;
    }
    public void calculate_diagonal_moves(int y_row_increment, int x_row_increment) {
        this.restart_loop();
        while (in_bounds && moveCounter < moveLimit) {
            y_row += y_row_increment;
            x_col += x_row_increment;
            this.processes_on_coordinate(y_row,x_col);
        }
    }
    public void processes_on_coordinate(int y_row,int x_col) {
        if (new CheckBounds().verify_out_of_bounds(y_row, x_col)) {
            this.in_bounds = false;
        } else {
            ChessPosition endPosition = new ChessPosition(y_row, x_col);
            ChessMove possible_move = new ChessMove(startPosition, endPosition, promotionPieceType);
            moveCollection.add(possible_move);
            moveCounter++;
        }
    }
}
