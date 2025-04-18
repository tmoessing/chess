package server.websocket;

import chess.*;
import dataaccess.SQLAuthDAO;
import com.google.gson.Gson;
import dataaccess.SQLGameDAO;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.Collection;

import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {

    private SQLAuthDAO authDAO = new SQLAuthDAO();
    private SQLGameDAO gameDAO = new SQLGameDAO();


    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {

        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        String username = authDAO.getUsernameViaAuthToken(command.getAuthToken());
        if (username == null || username.equals("null")) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Bad AuthToken"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }

        int gameID = command.getGameID();
        ChessGame chessGame = gameDAO.getGameBoard(gameID);

        if (chessGame == null) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Bad GameID"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }

        switch (command.getCommandType()) {
            case CONNECT -> connect(session, gameID, username, chessGame);
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(session, chessGame, gameID, username, makeMoveCommand);
            }
            case LEAVE -> leave(gameID, username);
            case RESIGN -> resign(session, chessGame, gameID, username);
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
    }

    private void connect(Session session, int gameID, String username, ChessGame chessGame) throws IOException {
        connections.add(username, gameID, session);
        ChessGame.TeamColor usernameColor = gameDAO.userColor(gameID, username);
        String usernameRole;
        if (usernameColor == null) {
            usernameRole = "observer";
        } else if (usernameColor.equals(ChessGame.TeamColor.WHITE)) {
            usernameRole = "white";
        } else {
            usernameRole = "black";
        }

        var message = new Gson().toJson(String.format("%s joined the game as %s", username, usernameRole));
        var serverMessageNotification = new Notification(NOTIFICATION, message);
        connections.broadcast(username, gameID, serverMessageNotification);
        var serverMessageLoadGame = new LoadGame(LOAD_GAME, chessGame);
        connections.broadcastRoot(username, serverMessageLoadGame);
    }

    public void makeMove(Session session, ChessGame chessGame, int gameID, String username, MakeMoveCommand command) throws IOException {
        // Verify Move
        ChessMove chessMove = command.getMove();
        ChessPosition startPosition = chessMove.getStartPosition();
        Collection<ChessMove> validMoves = chessGame.validMoves(startPosition);
        ChessPiece chessPiece = chessGame.getBoard().getPiece(startPosition);
        ChessGame.TeamColor chessPieceColor = chessPiece.getTeamColor();
        ChessGame.TeamColor teamTurnColor = chessGame.getTeamTurn();
        ChessGame.TeamColor userColor = gameDAO.userColor(gameID, username);

        if (chessPieceColor != userColor) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                    new Gson().toJson("Error: Attempted to Move Opponent"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }

        if (chessGame.isGameStateOver()) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                    new Gson().toJson("Error: Attempted Move After Game End"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }


        // Make Move
        try {
            chessGame.makeMove(chessMove);



            // Check if in CheckMate
            if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                chessGame.setGameStateOver(true);
                var message = new Gson().toJson(String.format("BLACK is in Checkmate", username));
                var serverCheckMateNotification = new Notification(NOTIFICATION, message);
                connections.broadcast(null, gameID, serverCheckMateNotification);
            } else if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                chessGame.setGameStateOver(true);
                var message = new Gson().toJson(String.format("WHITE is in Checkmate", username));
                var serverCheckMateNotification = new Notification(NOTIFICATION, message);
                connections.broadcast(null, gameID,  serverCheckMateNotification);
            } else {
                // Check if in Check
                if (chessGame.isInCheck(ChessGame.TeamColor.WHITE)) {
                    var message = new Gson().toJson(String.format("WHITE is in Check"));
                    var serverCheckNotification = new Notification(NOTIFICATION, message);
                    connections.broadcast(null, gameID,  serverCheckNotification);
                } else if (chessGame.isInCheck(ChessGame.TeamColor.BLACK)) {
                    var message = new Gson().toJson(String.format("BLACK is in Check"));
                    var serverCheckNotification = new Notification(NOTIFICATION, message);
                    connections.broadcast(null, gameID,  serverCheckNotification);
                }
            }

            gameDAO.updateGame(gameID, chessGame);
        } catch (InvalidMoveException e) {
            if (!validMoves.contains(chessMove)) {
                var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Invalid Move"));
                session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
                return;
            } else if (userColor == null) {
                var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("You are an observer"));
                session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
                return;
            } else if (chessPieceColor != teamTurnColor) {
                var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Wrong Turn"));
                session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
                return;
            } else {
                var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson(e.getMessage()));
                session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
                return;
            }
        }

        var message = new Gson().toJson(String.format("%s made move %s", username, stringifyMove(chessMove)));
        var serverMessageNotification = new Notification(NOTIFICATION, message);
        connections.broadcast(username, gameID,  serverMessageNotification);
        var serverMessageLoadGame = new LoadGame(LOAD_GAME, chessGame);
        connections.broadcast(null, gameID, serverMessageLoadGame);
    }

    private String stringifyMove(ChessMove chessMove) {
        ChessPosition startPos = chessMove.getStartPosition();
        ChessPosition endPos = chessMove.getEndPosition(); // Fixed

        String startPosString = colGetter(startPos) + String.valueOf(startPos.getRow());
        String endPosString = colGetter(endPos) + String.valueOf(endPos.getRow());

        return startPosString + " to " + endPosString;
    }

    private char colGetter(ChessPosition chessPosition) {
        switch (chessPosition.getCol()) {
            case 1 -> {return 'a';}
            case 2 -> {return 'b';}
            case 3 -> {return 'c';}
            case 4 -> {return 'd';}
            case 5 -> {return 'e';}
            case 6 -> {return 'f';}
            case 7 -> {return 'g';}
            case 8 -> {return 'h';}
            default -> {return ' ';}
        }
    }

    private void leave(int gameID, String username) throws IOException {
        ChessGame.TeamColor userColor = gameDAO.userColor(gameID, username);
        if (userColor != null) {
            gameDAO.userLeaveGame(gameID, userColor);
        }

        connections.leave(gameID, username);

        var message = new Gson().toJson(String.format("%s left the game.", username));
        var serverMessage = new Notification(NOTIFICATION, message);
        connections.broadcast(null, gameID, serverMessage);
    }

    private void resign(Session session, ChessGame chessGame, int gameID, String username) throws IOException {
        ChessGame.TeamColor userColor = gameDAO.userColor(gameID, username);
        if (userColor == null) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Observer tried to leave"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }

        if (chessGame.isGameStateOver()) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Game is already Over"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }

        chessGame.setGameStateOver(true);
        gameDAO.updateGame(gameID, chessGame);
        var message = new Gson().toJson(String.format("%s has resigned.", username));
        var serverMessage = new Notification(NOTIFICATION, message);
        connections.broadcast(null, gameID, serverMessage);
        connections.endGame(gameID);
    }
}