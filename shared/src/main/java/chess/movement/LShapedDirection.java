package chess.movement;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;

public class LShapedDirection {
   public Collection<ChessMove> moveCollection;
   private ChessPosition startPosition;
   private ChessPiece.PieceType promotionType = null;


   public LShapedDirection(Collection<ChessMove> moveCollection, ChessPosition startPosition) {
       this.moveCollection = moveCollection;
       this.startPosition = startPosition;
   }



   public void calculate_lshaped_moves() {
       int y_row = startPosition.getRow();
       int x_col = startPosition.getColumn();

       List<int[]> lShapedMoves = new ArrayList<>();
       lShapedMoves.add(new int[]{y_row + 2, x_col + 1});
       lShapedMoves.add(new int[]{y_row + 2, x_col - 1});
       lShapedMoves.add(new int[]{y_row - 2, x_col + 1});
       lShapedMoves.add(new int[]{y_row - 2, x_col - 1});
       lShapedMoves.add(new int[]{y_row - 1, x_col + 2});
       lShapedMoves.add(new int[]{y_row + 1, x_col + 2});
       lShapedMoves.add(new int[]{y_row - 1, x_col - 2});
       lShapedMoves.add(new int[]{y_row + 1, x_col - 2});

       for (int i=0; i < lShapedMoves.size(); i++) {
         int[] move = lShapedMoves.get(i);
         int row = move[0];
         int col = move[1];
         ChessPosition endPosition = new ChessPosition(row, col);

         if (new CheckBounds().verify_out_of_bounds(row, col)) {
             return;
         }
         else {
             ChessMove possible_move = new ChessMove(startPosition, endPosition, promotionType);
             moveCollection.add(possible_move);
         }
       }
   }


}

