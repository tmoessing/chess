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

    public void calculateDiagonalMoves(int yRowI, int xColI){
        super.setLoop();
        int yRow = this.chessPosition.getRow();
        int xCol = this.chessPosition.getCol();

        while (inBounds & notBlocked & (moveCounter < moveLimit)){
            yRow += yRowI;
            xCol += xColI;

            ChessPosition possiblePosition = new ChessPosition(yRow, xCol);
            super.handlePossibleMove(this.chessPosition, possiblePosition);
        }
    }
}
