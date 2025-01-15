package chess;

import java.util.ArrayList;

public class PieceMoveCalculator {
    private ArrayList<ChessPosition> possible_moves;
    private ChessPiece.PieceType chess_piece;
    private ChessPosition myPosition;

    public PieceMoveCalculator(ChessPiece chess_piece_object, ChessPosition myPosition) {
        this.chess_piece = chess_piece_object.getPieceType();
        this.myPosition = myPosition;
    }

    public void calculate() {
//        if (chess_piece == ChessPiece.PieceType.KING) {
//
//        }
//        else if (chess_piece == ChessPiece.PieceType.QUEEN) {
//
//        }
        if (chess_piece == ChessPiece.PieceType.BISHOP) {
                this.possible_moves = new DiagonalDirection(this.myPosition);

        }
//        else if (chess_piece == ChessPiece.PieceType.KNIGHT) {
//
//        }
    }
}
