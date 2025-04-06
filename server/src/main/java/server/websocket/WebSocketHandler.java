package server.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import dataaccess.SQLAuthDAO;
import com.google.gson.Gson;
import dataaccess.SQLGameDAO;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;

@WebSocket
public class WebSocketHandler {

    private SQLAuthDAO authDAO = new SQLAuthDAO();
    private SQLGameDAO gameDAO = new SQLGameDAO();


    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        String username = authDAO.getUsernameViaAuthToken(command.getAuthToken());
        int gameID = command.getGameID();

        String chessBoardString = new Gson().toJson(gameDAO.getGameBoard(gameID));

        switch (command.getCommandType()) {
            case CONNECT -> connect(session, gameID, username, chessBoardString);
            case MAKE_MOVE -> makeMove(command.getAuthToken());
            case LEAVE -> leave(session, gameID, username);
            case RESIGN -> resign(command.getAuthToken(), command.getGameID());
        }
    }

    private void connect(Session session, int gameID, String username, String chessBoardString) throws IOException {
        connections.add(username, gameID, session);
        var message = new Gson().toJson(String.format("%s joined the game", username));
        var serverMessageNotification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        //        connections.broadcast(username, serverMessage);
        connections.broadcast(null, serverMessageNotification);
        var serverMessageLoadGame = new Notification(ServerMessage.ServerMessageType.LOAD_GAME, chessBoardString);
        connections.broadcast(null, serverMessageLoadGame);

    }

    public void makeMove(String username) throws IOException {
        var message = new Gson().toJson(String.format("%s made move ", username));
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(null, serverMessage);
    }

    private void leave(Session session, int gameID, String userName) throws IOException {
        connections.leave(gameID, userName);
//        var message = String.format("%s left the game", authToken);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(userName, serverMessage);
    }

    private void resign(String authToken, Integer gameID) throws IOException {
        connections.endGame(gameID);
//        var message = String.format("%s resigned", authToken);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(authToken, serverMessage);
    }
}