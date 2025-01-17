package chess;

import chess.movement.PieceMoveCalculator;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public String toString() {
        Map<PieceType, Character> pieceMap = Map.of(PieceType.PAWN, 'p', PieceType.KNIGHT, 'n', PieceType.ROOK, 'r', PieceType.QUEEN, 'q', PieceType.KING, 'k', PieceType.BISHOP, 'b');
        Character toPrint = pieceMap.get(type);
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            return String.format("%s", toPrint);
        } else {
            return String.format("%s", Character.toUpperCase(toPrint));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } if (this == obj) {
            return true;
        } if (this.getClass() != obj.getClass()) {
            return false;
        }
        ChessPiece cp = (ChessPiece) obj;
        return this.pieceColor == cp.pieceColor && this.type == cp.type;
    }

    @Override
    public int hashCode() {
        return this.pieceColor.hashCode() + this.type.hashCode();
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor(){
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //Calculate Moves
        PieceMoveCalculator possible_moves_object = new PieceMoveCalculator(this, myPosition);
        possible_moves_object.calculate();

        // Set Moves and Return
        Collection<ChessMove> moveCollection = possible_moves_object.moveCollection;
        return moveCollection;
    }
}
