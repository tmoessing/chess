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

    // EnPassant Variables
    private ChessMove lastChessMove;
    private boolean canEnPassant = false;
    private ChessPosition pawnPosition;

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

    public ChessPosition getTeamKingPosition(TeamColor teamColor) {
        // Set's King Position
        ChessPosition kingPosition;
        if (teamColor == TeamColor.WHITE){kingPosition = this.whiteKing;} else {kingPosition = this.blackKing;}
        return kingPosition;
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

    public Collection<ChessMove> createValidTeamColorChessMoveCollection(TeamColor teamColor){
        Collection<ChessMove> validTeamColorChessMovesCollection = new ArrayList<ChessMove>();
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

                // Check EnPassant Conditions
                this.setCanEnPassant(indexChessPosition, chessPiece);
                if (this.canEnPassant) {
                    ChessMove enPassantMove = this.createEnPassantMove(indexChessPosition, chessPiece);
                    validTeamColorChessMovesCollection.add(enPassantMove);
                }

                // Add Chess Piece Valid Moves Collection to validTeamColorChessMovesCollection
                Collection<ChessMove> validChessPieceMovesCollection = chessPiece.pieceMoves(this.chessBoard, indexChessPosition);
                validTeamColorChessMovesCollection.addAll(validChessPieceMovesCollection);
            }
        }

        return validTeamColorChessMovesCollection;
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
        TeamColor chessPieceColor = chessPiece.getTeamColor();
        Collection<ChessMove> validTeamColorChessMovesCollection = chessPiece.pieceMoves(this.chessBoard, startPosition);
        Collection<ChessMove> newValidTeamColorChessMovesCollection = new ArrayList<>();

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

        // Check EnPassant Conditions
        this.setCanEnPassant(startPosition, chessPiece);
        if (this.canEnPassant) {
            ChessMove enPassantMove = this.createEnPassantMove(startPosition, chessPiece);
            newValidTeamColorChessMovesCollection.add(enPassantMove);
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

        // Make sure piece exists in location
        if (chessPieceObject == null){throw new InvalidMoveException("No piece is start position");}
        ChessPiece.PieceType chessPiece = chessPieceObject.getPieceType();
        TeamColor chessPieceColor = chessPieceObject.getTeamColor();
        ChessPiece.PieceType chessPiecePromotion = move.getPromotionPiece();

        // Check Right Color Piece is moving
        if (chessPieceColor != this.currentTurn) {
            throw new InvalidMoveException("It is the other color's turn");
        }

        Collection<ChessMove> chessMoveCollection = validMoves(startPosition);

        //Check for Valid Move
        if (!chessMoveCollection.contains(move)) {
            throw new InvalidMoveException("Move given is not Valid");
        }

        // Handle Pawn Promotion
        if (chessPiecePromotion != null) {
            chessPieceObject = new ChessPiece(chessPieceColor, chessPiecePromotion);
        }

        // Check if team is in check
        if (isInCheck(chessPieceColor)) {throw new InvalidMoveException("You are in check and can't move this piece");}

        // Check if EnPassant Conditions is true and see if given move is enPassant move
        this.setCanEnPassant(startPosition, chessPieceObject);
        if (this.canEnPassant) {
            ChessMove enPassantMove = this.createEnPassantMove(startPosition, chessPieceObject);
            if (enPassantMove.equals(move)) {
                //Remove Pawn
                this.chessBoard.addPiece(this.pawnPosition, null);
            }
        }

        // Move Piece
        this.chessBoard.addPiece(startPosition, null);
        this.chessBoard.addPiece(endPosition, chessPieceObject);

        // Save past move
        this.lastChessMove = move;

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
        Collection<ChessMove> othervalidTeamColorValidChessMovesCollection =
                this.createValidTeamColorChessMoveCollection(
                        this.getOtherTeamColor(teamColor)
                );
        Collection<ChessPosition> othervalidTeamColorChessEndPositionCollection = new ArrayList<ChessPosition>();
        ChessPosition kingPosition = this.getTeamKingPosition(teamColor);

        // Grab End Positions from ChessMoves
        for (ChessMove chessMoveIndex : othervalidTeamColorValidChessMovesCollection) {
            othervalidTeamColorChessEndPositionCollection.add(chessMoveIndex.getEndPosition());
        }

        //Check if any of the other opponent's chess pieces contain the current teams king position
        return othervalidTeamColorChessEndPositionCollection.contains(kingPosition);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // Check if all team colors pieces valid pieces contain a move that puts out of check or blocks check from happening
        Collection<ChessMove> teamColorValidChessMoveCollection = this.createValidTeamColorChessMoveCollection(teamColor);
        return this.doesValidMovesContainNoCheck(teamColor, teamColorValidChessMoveCollection);
    }

    public boolean doesValidMovesContainNoCheck(TeamColor teamColor, Collection<ChessMove> teamColorValidChessMoveCollection ) {
        // Iterate through each possible chess move. Add chess moves to collection of valid moves when other pieces are on the board
        for (ChessMove chessMoveIndex : teamColorValidChessMoveCollection) {
            ChessPosition startPosition = chessMoveIndex.getStartPosition();
            ChessPiece chessPiece = this.chessBoard.getPiece(startPosition);

            // Make Chess Move
            this.chessBoard.addPiece(startPosition, null);

            // Store ChessPiece in endPosition
            ChessPiece chessPieceInEndPosition = this.chessBoard.getPiece(chessMoveIndex.getEndPosition());
            this.chessBoard.addPiece(chessMoveIndex.getEndPosition(), chessPiece);

            if (!isInCheck(teamColor)) {
                return false;
            }

            // Revert Chess Move
            this.chessBoard.addPiece(startPosition, chessPiece);
            this.chessBoard.addPiece(chessMoveIndex.getEndPosition(), chessPieceInEndPosition);
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> teamColorValidChessMoveCollection = this.createValidTeamColorChessMoveCollection(teamColor);
        if (isInCheck(teamColor)) {
            return false;
        } else {
            return this.doesValidMovesContainNoCheck(teamColor, teamColorValidChessMoveCollection);
        }
    }

    public void setCanEnPassant(ChessPosition startPosition, ChessPiece chessPiece) {
        // Check for a previous move
        if (this.lastChessMove != null) {
            ChessPiece lastChessPieceMoved = this.chessBoard.getPiece(this.lastChessMove.getEndPosition());
            // Check if last piece moved is a pawn and moved twice
            if ((lastChessPieceMoved.getPieceType() == ChessPiece.PieceType.PAWN) &&
                    (Math.abs(this.lastChessMove.getStartPosition().getRow() -
                            this.lastChessMove.getEndPosition().getRow()) == 2)) {
                // Check if current piece is a pawn on same row and column +- 1 as well as opposite color
                if ((chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) &&
                        (this.lastChessMove.getEndPosition().getRow() == startPosition.getRow()) &&
                        (Math.abs(this.lastChessMove.getEndPosition().getColumn() - startPosition.getColumn()) == 1)
                        && (lastChessPieceMoved.getTeamColor() != chessPiece.getTeamColor())) {
                    this.canEnPassant = true;
                    this.pawnPosition = this.lastChessMove.getEndPosition();
                    return;
                }
            }
        }
        this.canEnPassant = false;
    }

    public ChessMove createEnPassantMove(ChessPosition startPosition, ChessPiece chessPiece) {
        // Add EnPassant as valid move
        int direction;

        if (chessPiece.getTeamColor() == TeamColor.WHITE) {direction = 1;} else {direction = -1;}
        ChessPosition enPassantEndPosition = new ChessPosition(this.lastChessMove.getEndPosition().getRow() + direction,
                this.lastChessMove.getEndPosition().getColumn());
        return new ChessMove(startPosition, enPassantEndPosition, null);
    }
}
