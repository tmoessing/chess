package chess.movement;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class DirectionCalculator {
    public Collection<ChessMove> moveCollection;
    public ChessPosition startPosition;
    public ChessPiece.PieceType promotionPieceType = null;
    public int moveLimit;

    // Calculate Loop Variables
    public boolean in_bounds;
    public int y_row;
    public int x_col;
    public int moveCounter;

    public void restart_loop () {
        y_row = startPosition.getRow();
        x_col = startPosition.getColumn();
        in_bounds = true;
        moveCounter = 0;
    }
    public void processes_on_coordinate(int y_row,int x_col) {
        if (y_row <= 0 || x_col <= 0 || y_row >= 9 || x_col >= 9) {
            this.in_bounds = false;
        } else {
            ChessPosition endPosition = new ChessPosition(y_row, x_col);
            ChessMove possible_move = new ChessMove(startPosition, endPosition, promotionPieceType);
            moveCollection.add(possible_move);
            moveCounter++;
        }
    }
}
