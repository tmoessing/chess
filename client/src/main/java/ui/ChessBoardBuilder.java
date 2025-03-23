package ui;

import java.util.List;

import static ui.EscapeSequences.*;

public class ChessBoardBuilder {
    private String[][] chessboard = new String[8][8];
    private String[][] chesspieces = new String[8][8];

    ChessBoardBuilder() {
        this.initializeBoard();
        this.initializePieces();
        this.drawBoard();
    }

    public void drawBoard() {
        System.out.print(EscapeSequences.moveCursorToLocation(0, 0));
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out.print(chessboard[row][col]);
                if (chesspieces[row][col] != null) {
                    System.out.print(chesspieces[row][col]);
                } else {
                    System.out.print(EMPTY);
                }
            }
            System.out.print(RESET_BG_COLOR + "\n");
        }
    }

    public void initializeBoard() {
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

    public void initializePieces() {

        String TOP_PAWN = SET_TEXT_COLOR_BLUE + BLACK_PAWN;
        String TOP_ROOK = SET_TEXT_COLOR_BLUE + BLACK_ROOK;
        String TOP_BISHOP = SET_TEXT_COLOR_BLUE + BLACK_BISHOP;
        String TOP_KNIGHT = SET_TEXT_COLOR_BLUE + BLACK_KNIGHT;
        String TOP_QUEEN = SET_TEXT_COLOR_BLUE + BLACK_QUEEN;
        String TOP_KING = SET_TEXT_COLOR_BLUE + BLACK_KING;

        String BOTTOM_PAWN = SET_TEXT_COLOR_RED + WHITE_PAWN;
        String BOTTOM_ROOK = SET_TEXT_COLOR_RED + WHITE_ROOK;
        String BOTTOM_BISHOP = SET_TEXT_COLOR_RED + WHITE_BISHOP;
        String BOTTOM_KNIGHT = SET_TEXT_COLOR_RED + WHITE_KNIGHT;
        String BOTTOM_QUEEN = SET_TEXT_COLOR_RED + WHITE_QUEEN;
        String BOTTOM_KING = SET_TEXT_COLOR_RED + WHITE_KING;

        List<Integer> rookCols = List.of(0, 7);
        List<Integer> knightCols = List.of(1,6);
        List<Integer> bishopCols = List.of(2, 5);

        // Set Pawns
        for (int col = 0; col < 8; col++) {
            chesspieces[1][col] = TOP_PAWN;
            chesspieces[6][col] = BOTTOM_PAWN;
        }

        // Set Rooks
        for (int rookColNum : rookCols) {
            chesspieces[0][rookColNum] = TOP_ROOK;
            chesspieces[7][rookColNum] = BOTTOM_ROOK;
        }

        // Set Knights
        for (int knightColNum : knightCols) {
            chesspieces[0][knightColNum] = TOP_KNIGHT;
            chesspieces[7][knightColNum] = BOTTOM_KNIGHT;
        }

        // Set Bishops
        for (int bishopColNum : bishopCols) {
            chesspieces[0][bishopColNum] = TOP_BISHOP;
            chesspieces[7][bishopColNum] = BOTTOM_BISHOP;
        }

        // Set Queens and Kings
        chesspieces[0][3] = TOP_QUEEN;
        chesspieces[0][4] = TOP_KING;

        chesspieces[7][3] = BOTTOM_QUEEN;
        chesspieces[7][4] = BOTTOM_KING;
    }
}

