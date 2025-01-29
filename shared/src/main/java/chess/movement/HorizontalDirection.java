package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class HorizontalDirection extends  DirectionCalculator{

    public HorizontalDirection(Collection<ChessMove> chessMoveCollection, ChessPiece chessPiece, ChessBoard chessBoard, ChessPosition chessPosition){
        super(chessMoveCollection,chessPiece,chessBoard,chessPosition);
    }

    public void calculate_moves(int moveLimit){
        this.moveLimit = moveLimit;
        this.calculate_horizontal_moves( 1);
        this.calculate_horizontal_moves(-1);
    }


    public void calculate_horizontal_moves(int x_col_i){
        super.set_loop();
        int y_row = this.chessPosition.getRow();
        int x_col = this.chessPosition.getCol();

        while (in_bounds & not_blocked & (moveCounter < moveLimit)){
            x_col += x_col_i;

            ChessPosition possiblePosition = new ChessPosition(y_row, x_col);
            super.handle_possible_move(this.chessPosition, possiblePosition);
        }
    }
}
