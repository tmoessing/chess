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

    public void calculateMoves(int moveLimit){
        this.moveLimit = moveLimit;
        super.calculateVerticalHorizontalMoves(0, 1);
        super.calculateVerticalHorizontalMoves(0, -1);
    }

    public void calculatePawnMoves(int moveLimit, boolean promotion, int direction){
        this.moveLimit = moveLimit;
        this.promotion = promotion;
        this.direction = direction;
        super.calculateVerticalHorizontalMoves(0, direction);
    }
}
