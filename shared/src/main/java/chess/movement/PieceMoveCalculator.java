package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {
   private Collection<ChessMove> chessMoveCollection = new ArrayList<>();
   private ChessPiece chessPiece;
   private ChessBoard chessBoard;
   private ChessPosition chessPosition;

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

    public PieceMoveCalculator(ChessPiece chessPiece, ChessBoard chessBoard, ChessPosition chessPosition) {
        setChessPiece(chessPiece);
        setChessBoard(chessBoard);
        setChessPosition(chessPosition);
   }

   public void calculateMoves() {
        if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            this.calculateDiagonalMoves(100);
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.KING) {
            this.calculateDiagonalMoves(1);
            this.calculateVerticalMoves(1);
            this.calculateHorizontalMoves(1);
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            this.calculateDiagonalMoves(100);
            this.calculateVerticalMoves(100);
            this.calculateHorizontalMoves(100);
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
            this.calculateVerticalMoves(100);
            this.calculateHorizontalMoves(100);
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            LDirection moveCalc = new LDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
            moveCalc.calculateMoves();
            this.chessMoveCollection = moveCalc.getChessMoveCollection();
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            this.calculatePawnMoves();
        }

   }

   public void calculateDiagonalMoves(int moveLimit) {
        DiagonalDirection moveCalc = new DiagonalDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
        moveCalc.calculateMoves(moveLimit);
        this.chessMoveCollection = moveCalc.getChessMoveCollection();
    }
    public void calculateHorizontalMoves(int moveLimit) {
        HorizontalDirection moveCalc = new HorizontalDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
        moveCalc.calculateMoves(moveLimit);
        this.chessMoveCollection = moveCalc.getChessMoveCollection();
    }
    public void calculateVerticalMoves(int moveLimit) {
        VerticalDirection moveCalc = new VerticalDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
        moveCalc.calculateMoves(moveLimit);
        this.chessMoveCollection = moveCalc.getChessMoveCollection();
    }
    public void calculatePawnMoves() {
        int direction;
        int moveLimit;
        boolean promotion;

        // PROMOTION
        promotion = (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE && chessPosition.getRow() == 7) || (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK && chessPosition.getRow() == 2);

        // MOVE LIMIT
        if ((chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE && chessPosition.getRow() == 2) || (chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK && chessPosition.getRow() == 7)){
            moveLimit = 2;
        } else {
            moveLimit = 1;
        }

        //DIRECTION
        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
        } else {
            direction = -1;
        }

        //Vertical
        VerticalDirection moveCalc = new VerticalDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
        moveCalc.calculatePawnMoves(moveLimit, promotion, direction);
        this.chessMoveCollection = moveCalc.getChessMoveCollection();

        moveLimit = 1;

        //Diagonal
        DiagonalDirection moveCalc2 = new DiagonalDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
        moveCalc2.calculatePawnMoves(moveLimit, promotion, direction);
        this.chessMoveCollection = moveCalc2.getChessMoveCollection();

    }
}
