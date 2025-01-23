package chess.movement;

import chess.*;
import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {
    private ChessBoard chessBoard;
    public Collection<ChessMove> moveCollection;
    private ChessPiece chessPiece;
    private ChessPosition myPosition;
    private ChessPiece.PieceType chessPieceType;


    public PieceMoveCalculator(chess.ChessBoard chessBoard, ChessPiece chessPiece, ChessPosition myPosition) {
        this.chessBoard = chessBoard;
        this.moveCollection = new ArrayList<ChessMove>();
        this.chessPiece = chessPiece;
        this.myPosition = myPosition;
        this.chessPieceType = chessPiece.getPieceType();
    }

    public void calculate() {
        if (chessPieceType == ChessPiece.PieceType.KING) {
            this.calculate_diagonal_moves(1);
            this.calculate_horizontal_moves(1);
            this.calculate_vertical_moves(1);
        } else if (chessPieceType == ChessPiece.PieceType.QUEEN) {
            this.calculate_diagonal_moves(100);
            this.calculate_horizontal_moves(100);
            this.calculate_vertical_moves(100);
        } else if (chessPieceType == ChessPiece.PieceType.BISHOP) {
            this.calculate_diagonal_moves(100);
        } else if (chessPieceType == ChessPiece.PieceType.KNIGHT) {
            LShapedDirection calculateMoves = new LShapedDirection(chessBoard, chessPiece, this.moveCollection, this.myPosition);
            calculateMoves.calculate_lshaped_moves();
            this.moveCollection = calculateMoves.moveCollection;
        } else if (chessPieceType == ChessPiece.PieceType.ROOK) {
            this.calculate_horizontal_moves(100);
            this.calculate_vertical_moves(100);
        } else if (chessPieceType == ChessPiece.PieceType.PAWN) {
            this.calculate_pawn_moves();
        }
    }
    public void calculate_diagonal_moves(int moveLimit) {
        // Diagonal
        DiagonalDirection calculateMoves = new DiagonalDirection(chessBoard, chessPiece, this.moveCollection, this.myPosition, moveLimit);
        calculateMoves.calculate_all_diagonal_moves();
        this.moveCollection = calculateMoves.moveCollection;
    }

    public void calculate_horizontal_moves(int moveLimit) {
        HorizontalDirection calculateMoves = new HorizontalDirection(chessBoard, chessPiece, this.moveCollection, this.myPosition, moveLimit);
        calculateMoves.calculate_all_horizontal_moves();
        this.moveCollection = calculateMoves.moveCollection;
    }

    public void calculate_vertical_moves(int moveLimit) {
        VerticalDirection calculateMoves = new VerticalDirection(chessBoard, chessPiece, this.moveCollection, this.myPosition, moveLimit);
        calculateMoves.calculate_all_vertical_moves();
        this.moveCollection = calculateMoves.moveCollection;
    }
    public void calculate_pawn_moves() {
        int move_limit;
        int direction;
        boolean promotion = false;

        // Deal with Promotion
        if (this.myPosition.getRow() == 7 && chessPiece.pieceColor == ChessGame.TeamColor.WHITE) {
            promotion = true;
        } else if (this.myPosition.getRow() == 2 && chessPiece.pieceColor == ChessGame.TeamColor.BLACK)
        {promotion = true;}

        // Deal with Starting Row
        if (this.myPosition.getRow() == 2 && chessPiece.pieceColor == ChessGame.TeamColor.WHITE) {
            move_limit = 2;
        } else if (this.myPosition.getRow() == 7 && chessPiece.pieceColor == ChessGame.TeamColor.BLACK) {
            move_limit = 2;}
        else {move_limit = 1;}

        // Deal with forward or backward
        if (chessPiece.pieceColor == ChessGame.TeamColor.WHITE) {direction = 1;} else {direction = -1;}

        // Vertical Movements
        VerticalDirection verticalMove = new VerticalDirection(chessBoard, chessPiece, moveCollection, this.myPosition, move_limit);
        if (promotion) {verticalMove.promotion = true;}
        verticalMove.calculate_pawn_vertical_moves(direction);

        // Diagonal Movements
        move_limit = 1;
        DiagonalDirection diagonalDirection = new DiagonalDirection(chessBoard, chessPiece, moveCollection, this.myPosition, move_limit);
        //  Set Pawn Specifcs
        if (promotion) {diagonalDirection.promotion = true;}
        diagonalDirection.diagonal_attack = true;

        diagonalDirection.calculate_all_diagonal_pawn_moves(direction);
    }
}
