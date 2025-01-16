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

    public DiagonalDirection(Collection<ChessMove> moveCollection,ChessPosition startPosition, int moveLimit) {
        this.moveCollection = moveCollection;
        this.startPosition = startPosition;
        this.moveLimit = moveLimit;
    }

    public void calculate_all_diagonal_moves() {
        this.upper_right_positions();
        this.upper_left_positions();
        this.lower_left_positions();
        this.lower_right_positions();
    }

    public void upper_right_positions() {
        boolean in_bounds = true;
        int y_row = startPosition.getRow();
        int x_col = startPosition.getColumn();
        int moveCounter = 0;

        while (in_bounds & (moveCounter < moveLimit)) {
            //Create Possible Position
            y_row = y_row + 1;
            x_col = x_col + 1;
            ChessPosition endPosition = new ChessPosition(y_row, x_col);

            //Check Bounds
            if (new CheckBounds().verify_out_of_bounds(y_row, x_col)) {
                in_bounds = false;
            } else {
                ChessMove possible_move = new ChessMove(startPosition, endPosition, promotionPieceType);
                moveCollection.add(possible_move);
                moveCounter++;
            }
        }
    }
    public void lower_left_positions() {
        boolean in_bounds = true;
        int y_row = startPosition.getRow();
        int x_col = startPosition.getColumn();
        int moveCounter = 0;

        while (in_bounds & (moveCounter < moveLimit)) {
            //Create Possible Position
            y_row = y_row - 1;
            x_col = x_col - 1;
            ChessPosition endPosition = new ChessPosition(y_row, x_col);

            //Check Bounds
            if (new CheckBounds().verify_out_of_bounds(y_row, x_col)) {
                in_bounds = false;
            }
            else {
                ChessMove possible_move = new ChessMove(startPosition, endPosition, promotionPieceType);
                moveCollection.add(possible_move);
                moveCounter++;
            }
        }
    }
    public void upper_left_positions() {
        boolean in_bounds = true;
        int y_row = startPosition.getRow();
        int x_col = startPosition.getColumn();
        int moveCounter = 0;

        while (in_bounds & (moveCounter < moveLimit)) {
            //Create Possible Position
            y_row = y_row + 1;
            x_col = x_col - 1;
            ChessPosition endPosition = new ChessPosition(y_row, x_col);

            //Check Bounds
            if (new CheckBounds().verify_out_of_bounds(y_row, x_col)) {
                in_bounds = false;
            }
            else {
                ChessMove possible_move = new ChessMove(startPosition, endPosition, promotionPieceType);
                moveCollection.add(possible_move);
                moveCounter++;
            }
        }
    }
    public void lower_right_positions() {
        boolean in_bounds = true;
        int y_row = startPosition.getRow();
        int x_col = startPosition.getColumn();
        int moveCounter = 0;

        while (in_bounds & (moveCounter < moveLimit)) {
            //Create Possible Position
            y_row = y_row - 1;
            x_col = x_col + 1;
            ChessPosition endPosition = new ChessPosition(y_row, x_col);

            //Check Bounds
            if (new CheckBounds().verify_out_of_bounds(y_row, x_col)) {
                in_bounds = false;
            }
            else {
                ChessMove possible_move = new ChessMove(startPosition, endPosition, promotionPieceType);
                moveCollection.add(possible_move);
                moveCounter++;
            }
        }
    }
}
