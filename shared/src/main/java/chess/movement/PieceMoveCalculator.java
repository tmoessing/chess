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
            DiagonalDirection calculateMoves = new DiagonalDirection(this.moveCollection, this.myPosition, 1);
            calculateMoves.calculate_all_diagonal_moves();
            this.moveCollection = calculateMoves.moveCollection;

            //   Horizontal and Vertical
        }
        else if (chessPieceType == ChessPiece.PieceType.QUEEN) {
            DiagonalDirection calculateMoves = new DiagonalDirection(this.moveCollection, this.myPosition, 100);
            calculateMoves.calculate_all_diagonal_moves();
            this.moveCollection = calculateMoves.moveCollection;

            //   Horizontal and Vertical
        }
        else if (chessPieceType == ChessPiece.PieceType.BISHOP) {
            DiagonalDirection calculateMoves = new DiagonalDirection(this.moveCollection, this.myPosition, 100);
            calculateMoves.calculate_all_diagonal_moves();
            this.moveCollection = calculateMoves.moveCollection;

        }
        else if (chessPieceType == ChessPiece.PieceType.KNIGHT) {
            LShapedDirection calculateMoves = new LShapedDirection(this.moveCollection, this.myPosition);
            calculateMoves.calculate_lshaped_moves();
            this.moveCollection = calculateMoves.moveCollection;
        }
    }
}
