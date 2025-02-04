package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard chessBoard;
    private TeamColor currentTurn;
    private ChessPosition blackKing;
    private ChessPosition whiteKing;

    // Castling Variables
    private boolean whiteKingMoved = false;
    private boolean whiteRRookMoved = false;
    private boolean whiteLRookMoved = false;
    private boolean blackKingMoved = false;
    private boolean blackRRookMoved = false;
    private boolean blackLRookMoved = false;

    // EnPassant Variables
    private ChessMove lastChessMove;

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team;
    }

    public TeamColor getOtherTeamColor(TeamColor teamColor){
        TeamColor otherTeamColor;
        if (teamColor == TeamColor.WHITE) {otherTeamColor = TeamColor.BLACK;} else {otherTeamColor = TeamColor.WHITE;}
        return otherTeamColor;
    }
    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.chessBoard;
    }

    public ChessGame() {
        chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        setBoard(chessBoard);

        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessPosition> createValidTeamColorChessPositionCollection(TeamColor teamColor){
        Collection<ChessMove> validTeamColorChessMovesCollection = new ArrayList<ChessMove>();
        Collection<ChessPosition> validTeamColorChessEndPositionCollection = new ArrayList<ChessPosition>();
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++){
                ChessPosition indexChessPosition = new ChessPosition(row, col);

                // Check if position holds piece
                ChessPiece chessPiece = this.chessBoard.getPiece(indexChessPosition);
                if (chessPiece == null){
                    continue;
                }

                // Get Piece Color
                TeamColor chessPieceColor = chessPiece.getTeamColor();

                // Update King Position
                if (chessPieceColor == TeamColor.WHITE && chessPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    this.whiteKing = indexChessPosition;
                } else if (chessPieceColor == TeamColor.BLACK && chessPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    this.blackKing = indexChessPosition;
                }

                // Continue if piece isn't color requested
                if (chessPieceColor != teamColor) {
                    continue;
                }



                // Add Chess Piece Valid Moves Collection to validTeamColorChessMovesCollection
                Collection<ChessMove> validChessPieceMovesCollection = chessPiece.pieceMoves(this.chessBoard, indexChessPosition);
                validTeamColorChessMovesCollection.addAll(validChessPieceMovesCollection);
            }
        }

        // Grab End Positions from ChessMoves
        for (ChessMove chessMoveIndex : validTeamColorChessMovesCollection) {
            validTeamColorChessEndPositionCollection.add(chessMoveIndex.getEndPosition());
        }
        return validTeamColorChessEndPositionCollection;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece chessPiece = this.chessBoard.getPiece(startPosition);
        ChessBoard chessBoardCopy = this.chessBoard;
        TeamColor chessPieceColor = chessPiece.getTeamColor();
        Collection<ChessMove> validTeamColorChessMovesCollection = chessPiece.pieceMoves(this.chessBoard, startPosition);
        Collection<ChessMove> newValidTeamColorChessMovesCollection = new ArrayList<ChessMove>();

        // Iterate through each possible chess move. Add chess moves to collection of valid moves when other pieces are on the board
        for (ChessMove chessMoveIndex : validTeamColorChessMovesCollection) {
            // Make Chess Move
            this.chessBoard.addPiece(startPosition, null);

            // Store ChessPiece in endPosition
            ChessPiece chessPieceInEndPosition = this.chessBoard.getPiece(chessMoveIndex.getEndPosition());
            this.chessBoard.addPiece(chessMoveIndex.getEndPosition(), chessPiece);

            if (!isInCheck(chessPieceColor)) {
                newValidTeamColorChessMovesCollection.add(chessMoveIndex);
            }

            // Revert Chess Move
            this.chessBoard.addPiece(startPosition, chessPiece);
            this.chessBoard.addPiece(chessMoveIndex.getEndPosition(), chessPieceInEndPosition);
        }

        return newValidTeamColorChessMovesCollection;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece chessPieceObject = this.chessBoard.getPiece(startPosition);
        ChessPiece.PieceType chessPiece = chessPieceObject.getPieceType();
        TeamColor chessPieceColor = chessPieceObject.getTeamColor();
        ChessPiece.PieceType chessPiecePromotion = move.getPromotionPiece();
        Collection<ChessMove> chessMoveCollection = validMoves(startPosition);

        // Make sure piece exists in location
//        if (chessPieceObject == null){
//            throw new InvalidMoveException("No piece is start position");
//        }

        // Check Right Color Piece is moving
        if (chessPieceColor != this.currentTurn) {
            throw new InvalidMoveException("It is the other color's turn");
        }

        //Check for Valid Move
        if (!chessMoveCollection.contains(move)) {
            throw new InvalidMoveException("Move given is not Valid");
        }

        // Handle Pawn Promotion
        if (chessPiecePromotion != null) {
            chessPieceObject = new ChessPiece(chessPieceColor, chessPiecePromotion);
        }

        // Check check
        if (isInCheck(chessPieceColor)) {
            throw new InvalidMoveException("This move puts you in check");
        }

        // Move Piece
        this.chessBoard.addPiece(startPosition, null);
        this.chessBoard.addPiece(endPosition, chessPieceObject);

        // Switch Color
        if (chessPieceColor == TeamColor.BLACK) {
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessPosition> teamColorValidChessPositionCollection = this.createValidTeamColorChessPositionCollection(this.getOtherTeamColor(teamColor));

        // Set's King Position
        ChessPosition kingPosition;
        if (teamColor == TeamColor.WHITE){
            kingPosition = this.whiteKing;
        } else {
            kingPosition = this.blackKing;
        }

        //Check if any of the other opponent's chess pieces contain the current teams king position
        return teamColorValidChessPositionCollection.contains(kingPosition);
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    public void canCastle(){

    }

    public void updateCastlingVariables(ChessMove lastChessMove) {
        ChessPiece lastChessPieceMoved = this.chessBoard.getPiece(this.lastChessMove.getEndPosition());
        if (!whiteKingMoved && (lastChessPieceMoved.getPieceType() == ChessPiece.PieceType.KING) && (lastChessPieceMoved.getTeamColor() == TeamColor.WHITE)) {
            whiteKingMoved = true;
        } else if (!blackKingMoved && (lastChessPieceMoved.getPieceType() == ChessPiece.PieceType.KING) && (lastChessPieceMoved.getTeamColor() == TeamColor.BLACK)) {
            blackKingMoved = true;
        }
    }

    public boolean canEnPassant(){
        ChessPiece lastChessPieceMoved = this.chessBoard.getPiece(this.lastChessMove.getEndPosition());
        if (lastChessPieceMoved.getPieceType() != ChessPiece.PieceType.PAWN) {
            return false;
        } else {
            //Check if Pawn Moved Forward Twice
            if ((lastChessPieceMoved.getTeamColor() == TeamColor.WHITE) && (this.lastChessMove.getStartPosition().getRow() == 2) && (this.lastChessMove.getEndPosition().getRow() == 4)) {
                return true;
            } else if ((lastChessPieceMoved.getTeamColor() == TeamColor.BLACK) && (this.lastChessMove.getStartPosition().getRow() == 7) && (this.lastChessMove.getEndPosition().getRow() == 5)) {
                return true;
            } else {
                return false;
            }
        }
    }

}
