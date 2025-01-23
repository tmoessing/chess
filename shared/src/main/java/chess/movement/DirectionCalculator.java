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

    //Pawn Specific
    public boolean diagonal_attack = false;
    public boolean promotion = false;

    public void restart_loop () {
        y_row = startPosition.getRow();
        x_col = startPosition.getColumn();
        in_bounds = true;
        not_blocked = true;
        moveCounter = 0;
    }

    public void add_move(ChessPosition possibleMove) {
        if (promotion){
            ChessMove possibleChessMove = new ChessMove(startPosition, possibleMove, ChessPiece.PieceType.QUEEN);
            moveCollection.add(possibleChessMove);
            possibleChessMove = new ChessMove(startPosition, possibleMove, ChessPiece.PieceType.ROOK);
            moveCollection.add(possibleChessMove);
            possibleChessMove = new ChessMove(startPosition, possibleMove, ChessPiece.PieceType.BISHOP);
            moveCollection.add(possibleChessMove);
            possibleChessMove = new ChessMove(startPosition, possibleMove, ChessPiece.PieceType.KNIGHT);
            moveCollection.add(possibleChessMove);
        } else {
            ChessMove possibleChessMove = new ChessMove(startPosition, possibleMove, promotionPieceType);
            moveCollection.add(possibleChessMove);
        }
        moveCounter++;
    }

    public void processes_on_coordinate(int y_row, int x_col) {
        // Check Bounds
        if (y_row <= 0 || x_col <= 0 || y_row >= 9 || x_col >= 9) {
            this.in_bounds = false;
            return; }

        // Check if Piece in Location
        ChessPosition possibleMove = new ChessPosition(y_row, x_col);
        ChessPiece potentialPiece = chessBoard.getPiece(possibleMove);

        if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            this.processes_on_pawn_coordinate(possibleMove, potentialPiece); return;}
        
        if (potentialPiece != null) {
            not_blocked = false;
            // Check if Blocked
            if (chessPiece.pieceColor == potentialPiece.pieceColor) {return;}
            // Piece can be Captured so move can be added
        }
        this.add_move(possibleMove);

    }

    public void processes_on_pawn_coordinate(ChessPosition possibleMove, ChessPiece potentialPiece) {
       if (diagonal_attack){
           if (potentialPiece != null) {
               if (chessPiece.pieceColor == potentialPiece.pieceColor) {return;}
               else {this.add_move(possibleMove);}
           } else {return;}
       } else if (potentialPiece != null) {
         not_blocked = false;
       } else {this.add_move(possibleMove);}
    }
}
