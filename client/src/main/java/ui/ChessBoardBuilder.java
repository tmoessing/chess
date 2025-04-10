package ui;

import chess.*;
import java.util.*;
import static ui.EscapeSequences.*;

public class ChessBoardBuilder {
    private final static String[] WHITE_ROW_HEADER = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final static String[] BLACK_ROW_HEADER = {"h", "g", "f", "e", "d", "c", "b", "a"};
    private final static String[] WHITE_COL_HEADER = {"8", "7", "6", "5", "4", "3", "2", "1"};
    private final static String[] BLACK_COL_HEADER = {"1", "2", "3", "4", "5", "6", "7", "8"};

    public ChessGame.TeamColor userPerspectiveColor;
    public ChessGame chessGame;

    private ChessBoard chessBoard;

    private String[][] clientChessBoard = new String[8][8];
    private String[][] clientChessPiece = new String[8][8];
    private String[][] clientBorder = new String[10][10];

    public ChessBoardBuilder(ChessGame chessGame, ChessGame.TeamColor userPerspectiveColor) {
        this.userPerspectiveColor = userPerspectiveColor;
        this.chessGame = chessGame;
        this.chessBoard = chessGame.getBoard();

        this.chessBoard.resetBoard();
    }

    public void run() {
        this.initializeBoard();
        this.initializePieces();
        this.initializeBorder();

        this.draw();
    }

    public void draw() {
        System.out.print("\nTurn: " + chessGame.getTeamTurn() + "\n");
        System.out.print(EscapeSequences.moveCursorToLocation(0, 0));
        this.drawBoard();
    }

    private void drawBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (clientBorder[row][col] != null) {
                    System.out.print(clientBorder[row][col]);
                }
                try {
                    System.out.print(clientChessBoard[row-1][col-1]);
                    if (clientChessPiece[row-1][col-1] != null) {
                        System.out.print(clientChessPiece[row-1][col-1]);
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
                if ((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0)){
                    square += SET_BG_COLOR_DARK_GREEN;
                } else {
                   square +=  SET_BG_COLOR_LIGHT_GREY;
                }
                clientChessBoard[row][col] = square;
            }
        }
    }

    private void initializePieces() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int userRow;
                int userCol;
                if (userPerspectiveColor == ChessGame.TeamColor.BLACK) {
                    userRow = Math.abs((row+1));
                    userCol = Math.abs((col+1));
                } else {
                    userRow = Math.abs((row) - 8);
                    userCol = Math.abs((col) - 8);
                }
                ChessPiece chessPiece = chessBoard.getPiece(new ChessPosition(userRow, userCol));
                if (chessPiece == null) {
                    clientChessPiece[row][col] = EMPTY;
                    continue;
                }
                ChessPiece.PieceType chessPieceType = chessPiece.getPieceType();
                ChessGame.TeamColor chessPieceColor = chessPiece.getTeamColor();
                if (chessPieceColor == ChessGame.TeamColor.WHITE) {
                    clientChessPiece[row][col] = SET_TEXT_COLOR_WHITE;
                } else {
                    clientChessPiece[row][col] = SET_TEXT_COLOR_BLACK;
                }

                if (chessPieceType == ChessPiece.PieceType.PAWN) {
                    clientChessPiece[row][col] += PAWN;
                } else if (chessPieceType == ChessPiece.PieceType.ROOK) {
                    clientChessPiece[row][col] += ROOK;
                } else if (chessPieceType == ChessPiece.PieceType.KNIGHT) {
                    clientChessPiece[row][col] += KNIGHT;
                } else if (chessPieceType == ChessPiece.PieceType.BISHOP) {
                    clientChessPiece[row][col] += BISHOP;
                } else if (chessPieceType == ChessPiece.PieceType.QUEEN) {
                    clientChessPiece[row][col] += QUEEN;
                } else if (chessPieceType == ChessPiece.PieceType.KING) {
                    clientChessPiece[row][col] += KING;
                }
            }
        }
        var x = 9;
    }

    private void initializeBorder() {
        String[] rowHeader;
        String[] colHeader;

        if (Objects.equals(this.userPerspectiveColor, ChessGame.TeamColor.WHITE)) {
            rowHeader = WHITE_ROW_HEADER;
            colHeader = WHITE_COL_HEADER;
        } else {
            rowHeader = BLACK_ROW_HEADER;
            colHeader = BLACK_COL_HEADER;
        }

       for (int col = 0; col < 10; col++) {
            String square = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_YELLOW;
            if (1 <= col & col <= 8 ){
                square +=  " " + rowHeader[col-1] + " ";
            } else {
                square += EMPTY;
            }
           clientBorder[0][col] = square;
           clientBorder[9][col] = square;
       }

       for (int row = 0; row < 10; row++) {
           String square = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_YELLOW;
           if (1 <= row & row <= 8 ){
               square += " " + colHeader[row-1] + " ";
           } else {
               square += EMPTY;
           }
           clientBorder[row][0] = square;
           clientBorder[row][9] = square;
       }
    }

    public int colNumber(char letter, ChessGame.TeamColor playerColor) {
        if (playerColor.equals(ChessGame.TeamColor.BLACK)) {
            return switch (letter) {
                case 'h' -> 1;
                case 'g' -> 2;
                case 'f' -> 3;
                case 'e' -> 4;
                case 'd' -> 5;
                case 'c' -> 6;
                case 'b' -> 7;
                case 'a' -> 8;
                default -> throw new IllegalStateException("Unexpected value: " + letter);
            };
        } else {
            return switch (letter) {
                case 'a' -> 1;
                case 'b' -> 2;
                case 'c' -> 3;
                case 'd' -> 4;
                case 'e' -> 5;
                case 'f' -> 6;
                case 'g' -> 7;
                case 'h' -> 8;
                default -> throw new IllegalStateException("Unexpected value: " + letter);
            };
        }
    }

    private boolean validatePosition(String pos) {
        if (pos.length() != 2) {
            System.out.println("Invalid use of Highlight");
            return false;
        }
        return true;
    }

    public int getRowFromPos(String pos, ChessGame.TeamColor playerColor) throws Exception {
        int rowInt;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            rowInt = Math.abs(Character.getNumericValue(pos.charAt(1)) - 9);
        } else {
            rowInt = Character.getNumericValue(pos.charAt(1));
        }
        if (rowInt == 0 || rowInt == 9) {
            throw new Exception("Invalid use of Highlight");
        }

        return rowInt;

    }

    public int getColFromPos(String pos, ChessGame.TeamColor playerColor) throws Exception {
        char colChar = pos.charAt(0);
        if (!Arrays.asList(WHITE_ROW_HEADER).contains(String.valueOf(colChar))) {
            throw new Exception("Invalid use of Highlight");
        }
        int colInt = colNumber(colChar, playerColor);
        return colInt;
    }

    public void highlightMoves(String pos, ChessGame.TeamColor playerColor) {
        // Remove Previous Comments
        this.initializeBoard();

        // Split pos into col and row
        if (!validatePosition(pos)) {
            return;
        }

        int rowInt;
        int colInt;
        try {
            rowInt = getRowFromPos(pos, playerColor);
            colInt = getColFromPos(pos, playerColor);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        ChessPiece chessPiece = this.chessBoard.getPiece(new ChessPosition(rowInt, colInt));

        if (chessPiece == null) {
            System.out.println("No Piece in Position");
            return;
        }

        clientChessBoard[rowInt-1][colInt-1] = SET_BG_COLOR_MAGENTA;

        Collection<ChessMove> possibleMoves = chessGame.validMoves(new ChessPosition(rowInt, colInt));
        for (ChessMove chessMove : possibleMoves) {
            ChessPosition chessPosition = chessMove.getEndPosition();
            clientChessBoard[chessPosition.getRow()-1][chessPosition.getColumn()-1] = SET_BG_COLOR_GREEN;
        }

        this.drawBoard();

    }

    public ChessMove makeMove(int gameID, String startPos, String endPos, ChessGame.TeamColor playerColor) {
        // Split pos into col and row
        if (!validatePosition(startPos) || !validatePosition(endPos)) {
            return null;
        }

        int startPosRowInt;
        int startPosColInt;
        int endPosRowInt;
        int endPosColInt;
        try {
            startPosRowInt = getRowFromPos(startPos, playerColor);
            startPosColInt = getColFromPos(startPos, playerColor);
            endPosRowInt = getRowFromPos(startPos, playerColor);
            endPosColInt = getColFromPos(startPos, playerColor);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        ChessPosition chessStartPosition = new ChessPosition(startPosRowInt, startPosColInt);
        ChessPosition chessEndPosition = new ChessPosition(endPosRowInt, endPosColInt);

        ChessPiece chessPiece = this.chessBoard.getPiece(chessStartPosition);

        // Handle Pawn Promotion
        ChessPiece.PieceType pawnPromotionPiece = null;

        if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (playerColor == ChessGame.TeamColor.WHITE && chessEndPosition.getRow() == 7) {
                Scanner scanner = new Scanner(System.in);
                while (pawnPromotionPiece == null) {
                    System.out.print("Pawn Promotion: Select Q(Queen), B(Bishop), R(Rook), K(Knight)");
                    String input = scanner.nextLine();

                    var tokens = input.toLowerCase().split(" ");
                    var cmd = (tokens.length > 0) ? tokens[0] : "help";
                    switch (cmd) {
                        case "Q" -> pawnPromotionPiece = ChessPiece.PieceType.QUEEN;
                        case "B" -> pawnPromotionPiece = ChessPiece.PieceType.BISHOP;
                        case "R" -> pawnPromotionPiece = ChessPiece.PieceType.ROOK;
                        case "K" -> pawnPromotionPiece = ChessPiece.PieceType.KNIGHT;
                        default ->  System.out.print(SET_TEXT_COLOR_WHITE + "Invalid Piece Selection\n" +
                                "Pawn Promotion: Select Q(Queen), B(Bishop), R(Rook), K(Knight)");
                    }
                }
            }
        }

        ChessMove chessMove = new ChessMove(chessStartPosition, chessEndPosition, pawnPromotionPiece);

        Collection<ChessMove> possibleMoves = chessGame.validMoves(chessStartPosition);
        if (!possibleMoves.contains(chessMove)) {
            System.out.println("Chess Move is not valid. Please Try Again");
            return null;
        }

        return chessMove;
    }
}

