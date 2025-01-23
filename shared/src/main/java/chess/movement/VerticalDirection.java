package chess.movement;

import chess.*;
import java.util.*;

public class VerticalDirection extends  DirectionCalculator {

   public VerticalDirection(ChessBoard chessBoard, ChessPiece chessPiece, Collection<ChessMove> moveCollection, ChessPosition startPosition, int moveLimit ) {
       this.chessBoard = chessBoard;
       this.chessPiece = chessPiece;
       this.startPosition = startPosition;
       this.moveCollection = moveCollection;
       this.moveLimit = moveLimit;
   }

   public void calculate_all_vertical_moves(){
      this.calculate_vertical_moves(1);
      this.calculate_vertical_moves(-1);
   }

   public void calculate_pawn_vertical_moves(int direction){
        this.calculate_vertical_moves(direction);
   }

   public void calculate_vertical_moves(int y_row_increment){
      super.restart_loop();
      while (in_bounds && not_blocked && moveCounter < moveLimit) {
         y_row += y_row_increment;
         super.processes_on_coordinate(y_row, x_col);
      }
   }
}
