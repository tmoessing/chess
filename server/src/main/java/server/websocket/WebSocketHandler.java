package server.websocket;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
//            case MAKE_MOVE -> makeMove(action.visitorName());
            case LEAVE -> leave(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
            case RESIGN -> resign(userGameCommand.getAuthToken(), userGameCommand.getGameID());
        }
    }


    private void connect(String authToken, int gameID, Session session) throws IOException {
        connections.add(authToken, gameID, session);
        var message = String.format("%s joined the game", authToken);
        var notification = new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(authToken, notification);
    }


    private void leave(String authToken, Integer gameID, Session session) throws IOException {
        connections.leave(authToken);
        var message = String.format("%s left the game", authToken);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(authToken, notification);
    }

    private void resign(String authToken, Integer gameID) throws IOException {
        connections.endGame(gameID);
        var message = String.format("%s resigned", authToken);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(authToken, notification);
    }
}