package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class DiagonalDirection extends DirectionCalculator {

    public DiagonalDirection(Collection<ChessMove> chessMoveCollection, ChessPiece chessPiece, ChessBoard chessBoard, ChessPosition chessPosition){
        super(chessMoveCollection,chessPiece,chessBoard,chessPosition);
    }

    public void calculateMoves(int moveLimit){
        this.moveLimit = moveLimit;
        this.calculateDiagonalMoves(1, 1);
        this.calculateDiagonalMoves(-1, -1);
        this.calculateDiagonalMoves(-1, 1);
        this.calculateDiagonalMoves(1, -1);
    }

    public void calculatePawnMoves(int moveLimit, boolean promotion, int direction){
        this.moveLimit = moveLimit;
        this.promotion = promotion;
        this.direction = direction;
        this.diagonal = true;
        this.calculateDiagonalMoves(direction, 1);
        this.calculateDiagonalMoves(direction, -1);
    }

    public void calculateDiagonalMoves(int y_row_i, int x_col_i){
        super.setLoop();
        int y_row = this.chessPosition.getRow();
        int x_col = this.chessPosition.getCol();

        while (in_bounds & not_blocked & (moveCounter < moveLimit)){
            y_row += y_row_i;
            x_col += x_col_i;

            ChessPosition possiblePosition = new ChessPosition(y_row, x_col);
            super.handlePossibleMove(this.chessPosition, possiblePosition);
        }
    }
}
