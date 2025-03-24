package ui;

import java.util.List;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoardBuilder {
    private final static String[] WHITE_ROW_HEADER = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final static String[] BLACK_ROW_HEADER = {"h", "g", "f", "e", "d", "c", "b", "a"};
    private final static String[] WHITE_COL_HEADER = {"8", "7", "6", "5", "4", "3", "2", "1"};
    private final static String[] BLACK_COL_HEADER = {"1", "2", "3", "4", "5", "6", "7", "8"};

    private final String color;

    private String[][] chessboard = new String[8][8];
    private String[][] chesspieces = new String[8][8];
    private String[][] border = new String[10][10];

    public ChessBoardBuilder(String color) {
        this.color = color;

    }

    public void run() {
        this.initializeBoard();
        this.initializePieces();
        this.initializeBorder();
        System.out.print(EscapeSequences.moveCursorToLocation(0, 0));
        this.drawBoard();
    }

    private void drawBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (border[row][col] != null) {
                    System.out.print(border[row][col]);
                }
                try {
                    System.out.print(chessboard[row-1][col-1]);
                    if (chesspieces[row-1][col-1] != null) {
                        System.out.print(chesspieces[row-1][col-1]);
                    } else {
                        System.out.print(EMPTY);
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
            System.out.print(RESET_BG_COLOR + "\n");
        }
    }

    private void initializeBoard() {
        for (int row = 0; row < 8 ; row++) {
            for (int col = 0; col < 8; col++) {
                String square = "";
                if ((row % 2 == 0 && col % 2 == 0) | (row % 2 != 0 && col % 2 != 0)){
                    square += SET_BG_COLOR_WHITE;
                } else {
                   square +=  SET_BG_COLOR_BLACK;
                }
                chessboard[row][col] = square;
            }
        }
    }

    private void setPieceColors() {

    }

    private void initializePieces() {
        String topColor;
        String bottomColor;

        if (Objects.equals(this.color, "WHITE")) {
            topColor = SET_TEXT_COLOR_RED;
            bottomColor = SET_TEXT_COLOR_BLUE;
        } else {
            topColor = SET_TEXT_COLOR_BLUE;
            bottomColor = SET_TEXT_COLOR_RED;
        }

        List<Integer> rookCols = List.of(0, 7);
        List<Integer> knightCols = List.of(1,6);
        List<Integer> bishopCols = List.of(2, 5);

        // Set Pawns
        for (int col = 0; col < 8; col++) {
            chesspieces[1][col] = topColor + PAWN;
            chesspieces[6][col] = bottomColor + PAWN;
        }

        // Set Rooks
        for (int rookColNum : rookCols) {
            chesspieces[0][rookColNum] = topColor + ROOK;
            chesspieces[7][rookColNum] = bottomColor + ROOK;
        }

        // Set Knights
        for (int knightColNum : knightCols) {
            chesspieces[0][knightColNum] = topColor + KNIGHT;
            chesspieces[7][knightColNum] = bottomColor + KNIGHT;
        }

        // Set Bishops
        for (int bishopColNum : bishopCols) {
            chesspieces[0][bishopColNum] = topColor + BISHOP;
            chesspieces[7][bishopColNum] = bottomColor + BISHOP;
        }

        // Set Queens and Kings
        chesspieces[0][3] = topColor + QUEEN;
        chesspieces[0][4] = topColor + KING;

        chesspieces[7][3] = bottomColor + QUEEN;
        chesspieces[7][4] = bottomColor + KING;
    }

    public void initializeBorder() {
        String[] rowHeader;
        String[] colHeader;

        if (Objects.equals(this.color, "WHITE")) {
            rowHeader = WHITE_ROW_HEADER;
            colHeader = WHITE_COL_HEADER;
        } else {
            rowHeader = BLACK_ROW_HEADER;
            colHeader = BLACK_COL_HEADER;
        }

       for (int col = 0; col < 10; col++) {
            String square = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK;
            if (1 <= col & col <= 8 ){
                square += rowHeader[col-1] + "  ";
            } else {
                square += EMPTY;
            }
           border[0][col] = square;
           border[9][col] = square;
       }

       for (int row = 0; row < 10; row++) {
           String square = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK;
           if (1 <= row & row <= 8 ){
               square += " " + colHeader[row-1] + "  ";
           } else {
               square += EMPTY;
           }
           border[row][0] = square;
           border[row][9] = square;
       }
    }
}

