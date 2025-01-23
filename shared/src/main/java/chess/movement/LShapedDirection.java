package chess.movement;

import chess.*;
import java.util.*;

public class LShapedDirection extends DirectionCalculator {

    public LShapedDirection(ChessBoard chessBoard, ChessPiece chessPiece, Collection<ChessMove> moveCollection, ChessPosition startPosition) {
        this.chessPiece = chessPiece;
        this.chessBoard = chessBoard;
        this.moveCollection = moveCollection;
        this.startPosition = startPosition;
    }

    public void calculate_lshaped_moves() {
        y_row = startPosition.getRow();
        x_col = startPosition.getColumn();

        List<int[]> lShapedMoves = new ArrayList<>();
        lShapedMoves.add(new int[]{y_row + 2, x_col + 1});
        lShapedMoves.add(new int[]{y_row + 2, x_col - 1});
        lShapedMoves.add(new int[]{y_row - 2, x_col + 1});
        lShapedMoves.add(new int[]{y_row - 2, x_col - 1});
        lShapedMoves.add(new int[]{y_row - 1, x_col + 2});
        lShapedMoves.add(new int[]{y_row + 1, x_col + 2});
        lShapedMoves.add(new int[]{y_row - 1, x_col - 2});
        lShapedMoves.add(new int[]{y_row + 1, x_col - 2});

        for (int[] move : lShapedMoves) {
            int row = move[0];
            int col = move[1];
            super.processes_on_coordinate(row, col);
        }
    }
}