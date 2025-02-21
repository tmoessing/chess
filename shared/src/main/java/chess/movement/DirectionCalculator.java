package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class DirectionCalculator {
    protected Collection<ChessMove> chessMoveCollection;
    protected ChessPiece chessPiece;
    protected ChessBoard chessBoard;
    protected ChessPosition chessPosition;
    protected ChessPiece.PieceType promotionPiece = null;

    protected int moveLimit;
    protected int moveCounter;
    protected boolean in_bounds;
    protected boolean not_blocked;

    //PAWN
    protected int direction;
    protected boolean promotion = false;
    protected boolean diagonal = false;

    public void setChessMoveCollection(Collection<ChessMove> chessMoveCollection) {
        this.chessMoveCollection = chessMoveCollection;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public void setChessPosition(ChessPosition chessPosition) {
        this.chessPosition = chessPosition;
    }

    public Collection<ChessMove> getChessMoveCollection() {
        return chessMoveCollection;
    }

    public DirectionCalculator(Collection<ChessMove> chessMoveCollection, ChessPiece chessPiece, ChessBoard chessBoard, ChessPosition chessPosition) {
        setChessMoveCollection(chessMoveCollection);
        setChessPiece(chessPiece);
        setChessBoard(chessBoard);
        setChessPosition(chessPosition);
    }

    public void setLoop(){
        this.moveCounter = 0;
        this.in_bounds = true;
        this.not_blocked = true;
    }

    public boolean checkBounds(ChessPosition possiblePosition){
        int y_row = possiblePosition.getRow();
        int x_col = possiblePosition.getColumn();
        return (y_row >= 1 & x_col >= 1 & y_row <=8 & x_col <=8);
    }

    public void addMove(ChessPosition startPosition, ChessPosition possiblePosition){
        if (this.promotion) {
            ChessMove chessMove1 = new ChessMove(startPosition, possiblePosition, ChessPiece.PieceType.QUEEN);
            ChessMove chessMove2 = new ChessMove(startPosition, possiblePosition, ChessPiece.PieceType.KNIGHT);
            ChessMove chessMove3 = new ChessMove(startPosition, possiblePosition, ChessPiece.PieceType.ROOK);
            ChessMove chessMove4 = new ChessMove(startPosition, possiblePosition, ChessPiece.PieceType.BISHOP);
            this.chessMoveCollection.add(chessMove1);
            this.chessMoveCollection.add(chessMove2);
            this.chessMoveCollection.add(chessMove3);
            this.chessMoveCollection.add(chessMove4);
        } else {
            ChessMove chessMove = new ChessMove(startPosition, possiblePosition, promotionPiece);
            this.chessMoveCollection.add(chessMove);
            this.moveCounter++;
        }
    }

    public void handlePossibleMove(ChessPosition startPosition, ChessPosition possiblePosition){
        this.in_bounds = this.checkBounds(possiblePosition);
        if (!in_bounds) {
            return;
        }

        if (this.chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            this.handlePawnMove(startPosition, possiblePosition);
            return;
        }

        if (chessBoard.getPiece(possiblePosition) != null){
            not_blocked = false;
            if (chessPiece.getTeamColor() == chessBoard.getPiece(possiblePosition).getTeamColor()){
                return;
            }
        }
        this.addMove(startPosition, possiblePosition);
    }

    public void handlePawnMove(ChessPosition startPosition, ChessPosition possiblePosition){
        if (diagonal){
            this.moveCounter++;
            if (chessBoard.getPiece(possiblePosition) != null) {
                not_blocked = false;
                if (chessPiece.getTeamColor() != chessBoard.getPiece(possiblePosition).getTeamColor()) {
                    this.addMove(startPosition, possiblePosition);
                }
            }
        } else {
            if (chessBoard.getPiece(possiblePosition) != null) {
                not_blocked = false;
                return;
            }
            this.addMove(startPosition, possiblePosition);
        }

    }
}
