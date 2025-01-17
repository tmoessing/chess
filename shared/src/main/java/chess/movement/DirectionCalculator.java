package chess.movement;

import chess.*;
import java.util.*;

public class DirectionCalculator {
    public ChessBoard chessBoard;
    public ChessPiece chessPiece;
    public Collection<ChessMove> moveCollection;
    public ChessPosition startPosition;
    public ChessPiece.PieceType promotionPieceType = null;
    public int moveLimit;

    // Calculate Loop Variables
    public boolean in_bounds;
    public boolean not_blocked;
    public int y_row;
    public int x_col;
    public int moveCounter;

    public void restart_loop () {
        y_row = startPosition.getRow();
        x_col = startPosition.getColumn();
        in_bounds = true;
        not_blocked = true;
        moveCounter = 0;
    }
    public void processes_on_coordinate(int y_row, int x_col) {
        // Check Bounds
        if (y_row <= 0 || x_col <= 0 || y_row >= 9 || x_col >= 9) {
            this.in_bounds = false;
            return; }

        // Check if Piece in Location
        ChessPosition possibleMove = new ChessPosition(y_row, x_col);
        ChessPiece potenialPiece = chessBoard.getPiece(possibleMove);

        if (potenialPiece != null) {
            // Check if Blocked
            if (chessPiece.pieceColor == potenialPiece.pieceColor) {
                not_blocked = false;
                return;
            }
            // Piece is Captureable
            not_blocked = false;
        }
        // Add Piece
        ChessMove possibleChessMove = new ChessMove(startPosition, possibleMove, promotionPieceType);
        moveCollection.add(possibleChessMove);
        moveCounter++;
    }
}
