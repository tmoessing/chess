package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {
   private Collection<ChessMove> chessMoveCollection = new ArrayList<ChessMove>();
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

   public void calculate_moves() {
        if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            this.calculate_diagonal_moves(100);
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.KING) {
            this.calculate_diagonal_moves(1);
            this.calculate_vertical_moves(1);
            this.calculate_horizontal_moves(1);
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            this.calculate_diagonal_moves(100);
            this.calculate_vertical_moves(100);
            this.calculate_horizontal_moves(100);
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
            this.calculate_vertical_moves(100);
            this.calculate_horizontal_moves(100);
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            LDirection moveCalc = new LDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
            moveCalc.calculate_moves();
            this.chessMoveCollection = moveCalc.getChessMoveCollection();
        } else if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            this.calculate_pawn_moves();
        }

   }

   public void calculate_diagonal_moves(int moveLimit) {
        DiagonalDirection moveCalc = new DiagonalDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
        moveCalc.calculate_moves(moveLimit);
        this.chessMoveCollection = moveCalc.getChessMoveCollection();
    }
    public void calculate_horizontal_moves(int moveLimit) {
        HorizontalDirection moveCalc = new HorizontalDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
        moveCalc.calculate_moves(moveLimit);
        this.chessMoveCollection = moveCalc.getChessMoveCollection();
    }
    public void calculate_vertical_moves(int moveLimit) {
        VerticalDirection moveCalc = new VerticalDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
        moveCalc.calculate_moves(moveLimit);
        this.chessMoveCollection = moveCalc.getChessMoveCollection();
    }
    public void calculate_pawn_moves() {
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
        moveCalc.calculate_pawn_moves(moveLimit, promotion, direction);
        this.chessMoveCollection = moveCalc.getChessMoveCollection();

        moveLimit = 1;

        //Diagonal
        DiagonalDirection moveCalc2 = new DiagonalDirection(chessMoveCollection, chessPiece, chessBoard, chessPosition);
        moveCalc2.calculate_pawn_moves(moveLimit, promotion, direction);
        this.chessMoveCollection = moveCalc2.getChessMoveCollection();

    }
}
