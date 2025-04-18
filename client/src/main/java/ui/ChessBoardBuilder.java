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

    public void updateGame(ChessGame chessGame, ChessGame.TeamColor userPerspectiveColor){
        this.userPerspectiveColor = userPerspectiveColor;
        this.chessGame = chessGame;
        this.chessBoard = chessGame.getBoard();
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
                    userCol = Math.abs((8-col));
                } else {
                    userRow = Math.abs((row)-8);
                    userCol = Math.abs((col+1));
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

    private boolean validatePosition(String pos) {
        if (pos.length() != 2) {
            System.out.println("Invalid use of command");
            return false;
        }
        return true;
    }

    public int getRowFromPos(String pos, ChessGame.TeamColor playerColor) throws Exception {
        int rowInt;
        rowInt = Math.abs(Character.getNumericValue(pos.charAt(1)) - 9);
        if (rowInt <= 0 || rowInt >= 9) {
            throw new Exception("Invalid use of command");
        }

        return rowInt;

    }

    public int getColFromPos(String pos, ChessGame.TeamColor playerColor) throws Exception {
        char colChar = pos.charAt(0);
        if (!Arrays.asList(WHITE_ROW_HEADER).contains(String.valueOf(colChar))) {
            throw new Exception("Invalid use of command");
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
            rowInt = getRowFromPos(pos, playerColor)-1;
            colInt = getColFromPos(pos, playerColor)-1;
        } catch (Exception e) {
            System.out.println("Invalid use of command");
            return;
        }

        ChessPosition chessPosition = new ChessPosition(8-rowInt, colInt+1);
        ChessPiece chessPiece = this.chessBoard.getPiece(chessPosition);

        if (chessPiece == null) {
            System.out.println("No Piece in Position");
            return;
        }

        if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
            clientChessBoard[rowInt][colInt] = SET_BG_COLOR_MAGENTA;
        }else {
            clientChessBoard[7-rowInt][7-colInt] = SET_BG_COLOR_MAGENTA;
        }


        Collection<ChessMove> possibleMoves = chessGame.validMoves(chessPosition);
        for (ChessMove chessMove : possibleMoves) {
            ChessPosition chessValidPosition = chessMove.getEndPosition();
            if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
                clientChessBoard[Math.abs(8 - chessValidPosition.getRow())][Math.abs(1 - chessValidPosition.getColumn())] = SET_BG_COLOR_GREEN;
            } else {
                clientChessBoard[Math.abs(1 - chessValidPosition.getRow())][Math.abs(8 - chessValidPosition.getColumn())] = SET_BG_COLOR_GREEN;
            }
        }

        this.drawBoard();

    }

    public ChessMove makeMove(int gameID, String startPos, String endPos, ChessGame.TeamColor playerColor) {
        // Split pos into col and row
        if (!validatePosition(startPos) || !validatePosition(endPos)) {
            return null;
        }

        // Create Board Move for Board
        int startPosRowInt;
        int startPosColInt;
        int endPosRowInt;
        int endPosColInt;
        try {
            startPosRowInt = getRowFromPos(startPos, playerColor)-1;
            startPosColInt = getColFromPos(startPos, playerColor)-1;
            endPosRowInt = getRowFromPos(endPos, playerColor)-1;
            endPosColInt = getColFromPos(endPos, playerColor)-1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        ChessPosition chessBoardStartPosition = new ChessPosition(8-startPosRowInt, startPosColInt+1);
        ChessPosition chessBoardEndPosition = new ChessPosition(8-endPosRowInt, endPosColInt+1);

        ChessPiece chessPiece = this.chessBoard.getPiece(chessBoardStartPosition);

        // Handle Pawn Promotion
        ChessPiece.PieceType pawnPromotionPiece = null;

        if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if ((playerColor == ChessGame.TeamColor.WHITE && chessBoardEndPosition.getRow() == 7) ||
                    (playerColor == ChessGame.TeamColor.BLACK && chessBoardEndPosition.getRow() == 2)) {
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

        return new ChessMove(chessBoardStartPosition, chessBoardEndPosition, pawnPromotionPiece);
    }
}