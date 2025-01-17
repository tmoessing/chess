package chess.movement;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import jdk.jshell.Diag;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {
    public Collection<ChessMove> moveCollection;
    private ChessPiece chessPiece;
    private ChessPosition myPosition;
    private ChessPiece.PieceType chessPieceType;

    public PieceMoveCalculator(ChessPiece chessPiece, ChessPosition myPosition) {
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
            LShapedDirection calculateMoves = new LShapedDirection(this.moveCollection, this.myPosition);
            calculateMoves.calculate_lshaped_moves();
            this.moveCollection = calculateMoves.moveCollection;
        } else if (chessPieceType == ChessPiece.PieceType.ROOK) {
            this.calculate_horizontal_moves(100);
            this.calculate_vertical_moves(100);
        } else if (chessPieceType == ChessPiece.PieceType.PAWN) {
            // NEED PAWN
        }
    }
    public void calculate_diagonal_moves(int moveLimit) {
        // Diagonal
        DiagonalDirection calculateMoves = new DiagonalDirection(this.moveCollection, this.myPosition, moveLimit);
        calculateMoves.calculate_all_diagonal_moves();
        this.moveCollection = calculateMoves.moveCollection;
    }

    public void calculate_horizontal_moves(int moveLimit) {
        HorizontalDirection calculateMoves = new HorizontalDirection(this.moveCollection, this.myPosition, moveLimit);
        calculateMoves.calculate_all_horizontal_moves();
        this.moveCollection = calculateMoves.moveCollection;
    }

    public void calculate_vertical_moves(int moveLimit) {
        VerticalDirection calculateMoves = new VerticalDirection(this.moveCollection, this.myPosition, moveLimit);
        calculateMoves.calculate_all_vertical_moves();
        this.moveCollection = calculateMoves.moveCollection;
    }

}
