package server.websocket;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        String username = getUsernameViaAuthToken(command.getAuthToken());
        int gameID = command.getGameID();

        switch (command.getCommandType()) {
            case CONNECT -> connect(session, gameID, username);
            case MAKE_MOVE -> makeMove(command.getAuthToken());
            case LEAVE -> leave(session, gameID, username);
            case RESIGN -> resign(command.getAuthToken(), command.getGameID());
        }
    }

    public String getUsernameViaAuthToken(String authToken) {
        String query = "SELECT username FROM auths WHERE authToken=? LIMIT 1";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {

            ps.setString(1, authToken);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    private void connect(Session session, int gameID, String username) throws IOException {
        connections.add(username, gameID, session);
        var message = new Gson().toJson(String.format("%s joined the game", username));
        var serverMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
//        connections.broadcast(username, serverMessage);
        connections.broadcast(null, serverMessage);
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