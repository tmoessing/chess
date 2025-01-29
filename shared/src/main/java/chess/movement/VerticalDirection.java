package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class VerticalDirection extends DirectionCalculator {

    public VerticalDirection(Collection<ChessMove> chessMoveCollection, ChessPiece chessPiece, ChessBoard chessBoard, ChessPosition chessPosition){
        super(chessMoveCollection,chessPiece,chessBoard,chessPosition);
    }

    public void calculate_moves(int moveLimit){
        this.moveLimit = moveLimit;
        this.calculate_vertical_moves(1);
        this.calculate_vertical_moves(-1);
    }

    public void calculate_pawn_moves(int moveLimit, boolean promotion, int direction){
        this.moveLimit = moveLimit;
        this.promotion = promotion;
        this.direction = direction;
        this.calculate_vertical_moves(direction);
    }

    public void calculate_vertical_moves(int y_row_i){
        super.set_loop();
        int y_row = this.chessPosition.getRow();
        int x_col = this.chessPosition.getCol();

        while (in_bounds & not_blocked & (moveCounter < moveLimit)){
            y_row += y_row_i;

            ChessPosition possiblePosition = new ChessPosition(y_row, x_col);
            super.handle_possible_move(this.chessPosition, possiblePosition);
        }
    }
}
