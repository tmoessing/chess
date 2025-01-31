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
    private Map<String, ChessPosition> chessBMap = new HashMap<>();
    private Collection<ChessPosition> blackValidMoves = new ArrayList<ChessPosition>();
    private Collection<ChessPosition> whiteValidMoves = new ArrayList<ChessPosition>();
    private Map<String, ChessPosition> chessWMap = new HashMap<>();
    private TeamColor currentTurn;

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

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++){
                ChessPosition indexChessPosition = new ChessPosition(row, col);
                ChessPiece chessPiece = board.getPiece(indexChessPosition);
                if (chessPiece == null){
                    continue;
                }

                TeamColor chessPieceColor = chessPiece.getTeamColor();
                Dictionary<ChessPiece.PieceType, ChessPosition> chessDict;

                StringBuilder result = new StringBuilder();
                if (chessPiece.getPieceType() == ChessPiece.PieceType.KING){
                    result.append(chessPiece.getPieceType());
                } else {
                    result.append(chessPiece.getPieceType()).append(col);
                }

                if (chessPieceColor == TeamColor.WHITE) {
                    chessWMap.put(result.toString(), indexChessPosition);
                    whiteValidMoves.addAll(this.receiveEndPositions(indexChessPosition));

                } else if (chessPieceColor == TeamColor.BLACK){
                    chessBMap.put(result.toString(), indexChessPosition);
                    blackValidMoves.addAll(this.receiveEndPositions(indexChessPosition));
                }
            }
        }
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

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece chessPiece = this.chessBoard.getPiece(startPosition);
        return chessPiece.pieceMoves(this.chessBoard, startPosition);
    }

    public Collection<ChessPosition> receiveEndPositions(ChessPosition startPosition) {
        Collection<ChessPosition> endChessPiecePositions = new ArrayList<ChessPosition>();
        Collection<ChessMove> chessPieceValidMoves = this.validMoves(startPosition);

        for (ChessMove chessMove : chessPieceValidMoves) {
            endChessPiecePositions.add(chessMove.getEndPosition());
        }
        return endChessPiecePositions;
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

        // Check check
//        if (isInCheck(chessPieceColor)) {
//            throw new InvalidMoveException("This move puts you in check");
//        }

        // Handle Pawn Promotion
        if (chessPiecePromotion != null) {
            chessPieceObject = new ChessPiece(chessPieceColor, chessPiecePromotion);
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
        ChessPosition kingPosition;

        if (teamColor == TeamColor.WHITE) {
            kingPosition = chessWMap.get("KING");
            if (blackValidMoves.contains(kingPosition)){
                return true;
            } else {
                return false;
            }
        } else {
            kingPosition = chessBMap.get("KING");
            if (whiteValidMoves.contains(kingPosition)){
                return true;
            } else {
                return false;
            }
        }
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

    public void canEnPassant(){

    }

}
