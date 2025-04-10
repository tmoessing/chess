package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[8][8];

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int row = -1; row < 8; row++) {
            for (int col = -1; col < 8; col++) {
                if (board[row][col] != null) {
                    sb.append("|").append(board[row][col]);
                } else {
                    sb.append("| ");
                }
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int yRow = 8-position.getRow();
        int xCol = position.getColumn()-1;
        board[yRow][xCol] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int yRow = Math.abs(8-position.getRow());
        int xCol = Math.abs(position.getColumn()-1);
        return board[yRow][xCol];
    }

    public ChessPiece getPieceRaw(ChessPosition position){
        int yRow = position.getRow();
        int xCol = position.getColumn();
        return board[yRow][xCol];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new ChessPiece[8][8];
        List<Integer> rookCols = List.of(0, 7);
        List<Integer> knightCols = List.of(1,6);
        List<Integer> bishopCols = List.of(2, 5);

        // Set Pawns
        for (int col = 0; col < 8; col++) {
            board[1][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            board[6][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        // Set Rooks
        for (int rookColNum : rookCols) {
            board[0][rookColNum] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            board[7][rookColNum] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        }

        // Set Knights
        for (int knightColNum : knightCols) {
            board[0][knightColNum] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            board[7][knightColNum] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        }

        // Set Bishops
        for (int bishopColNum : bishopCols) {
            board[0][bishopColNum] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            board[7][bishopColNum] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        }

        // Set Queens and Kings
        board[0][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[0][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

        board[7][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[7][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
    }

}