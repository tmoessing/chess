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
        String chessGameString = new Gson().toJson(chessGame);

        if (chessGameString == null || chessGameString.equals("null")) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Bad GameID"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }

        switch (command.getCommandType()) {
            case CONNECT -> connect(session, gameID, username, chessGameString);
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(session, chessGame, gameID, username, makeMoveCommand);
            }
            case LEAVE -> leave(session, gameID, username);
            case RESIGN -> resign(session, gameID, username);
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
    }

    private void connect(Session session, int gameID, String username, String chessGameString) throws IOException {
        connections.add(username, gameID, session);
        var message = new Gson().toJson(String.format("%s joined the game", username));
        var serverMessageNotification = new Notification(NOTIFICATION, message);
        connections.broadcast(username, serverMessageNotification);
        var serverMessageLoadGame = new LoadGame(LOAD_GAME, chessGameString);
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


        if (!validMoves.contains(chessMove)) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Invalid Move"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }
        if (userColor == null) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("You are an observer"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }
        if (chessPieceColor != userColor) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Attempting to Move Opponent"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }
        if (chessPieceColor != teamTurnColor) {
            var serverMessageNotification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new Gson().toJson("Wrong Turn"));
            session.getRemote().sendString(new Gson().toJson(serverMessageNotification));
            return;
        }

        // Make Move
        try {
            chessGame.makeMove(chessMove);
        } catch (InvalidMoveException e) {
            return;
        }
        gameDAO.updateGame(gameID, chessGame);


        String chessGameString = new Gson().toJson(chessGame);
        var message = new Gson().toJson(String.format("%s made move ", username));
        var serverMessageNotification = new Notification(NOTIFICATION, message);
        connections.broadcast(username, serverMessageNotification);
        var serverMessageLoadGame = new LoadGame(LOAD_GAME, chessGameString);
        connections.broadcast(null, serverMessageLoadGame);
    }

    private void leave(Session session, int gameID, String userName) throws IOException {
        connections.leave(gameID, userName);
        var message = new Gson().toJson(String.format("%s left the game.", userName));
        var serverMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(userName, serverMessage);
    }

    private void resign(Session session, int gameID, String userName) throws IOException {
        connections.endGame(gameID);
        var message = new Gson().toJson(String.format("%s has resigned.", userName));
        var serverMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(userName, serverMessage);
    }
}