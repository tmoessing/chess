package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LDirection extends DirectionCalculator {

    public LDirection(Collection<ChessMove> chessMoveCollection, ChessPiece chessPiece, ChessBoard chessBoard, ChessPosition chessPosition){
        super(chessMoveCollection,chessPiece,chessBoard,chessPosition);
    }

    public void calculateMoves(){
        super.setLoop();
        int y_row = this.chessPosition.getRow();
        int x_col = this.chessPosition.getCol();

        List<ChessPosition> positionList = new ArrayList<>();
        positionList.add(new ChessPosition(y_row+2, x_col+1));
        positionList.add(new ChessPosition(y_row+2, x_col-1));
        positionList.add(new ChessPosition(y_row-2, x_col+1));
        positionList.add(new ChessPosition(y_row-2, x_col-1));
        positionList.add(new ChessPosition(y_row+1, x_col+2));
        positionList.add(new ChessPosition(y_row-1, x_col+2));
        positionList.add(new ChessPosition(y_row+1, x_col-2));
        positionList.add(new ChessPosition(y_row-1, x_col-2));

        for (ChessPosition possiblePosition : positionList){
            super.handlePossibleMove(this.chessPosition, possiblePosition);
        }
    }
}
