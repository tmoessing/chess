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

    public void calculateMoves(int moveLimit){
        this.moveLimit = moveLimit;
        this.calculateHorizontalMoves( 1);
        this.calculateHorizontalMoves(-1);
    }


    public void calculateHorizontalMoves(int x_col_i){
        super.setLoop();
        int y_row = this.chessPosition.getRow();
        int x_col = this.chessPosition.getCol();

        while (in_bounds & not_blocked & (moveCounter < moveLimit)){
            x_col += x_col_i;

            ChessPosition possiblePosition = new ChessPosition(y_row, x_col);
            super.handlePossibleMove(this.chessPosition, possiblePosition);
        }
    }
}
