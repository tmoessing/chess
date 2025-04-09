package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.*;
import websocket.messages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        //Endpoint requires this method, but you don't have to do anything
    }

    public void enterChessGame(String authToken, int gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException e) {
            System.err.println("WebSocket send/close failed: " + e.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove chessMove) {
        try {
            var action = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, chessMove);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException e) {
            System.err.println("WebSocket send/close failed: " + e.getMessage());
        }
    }

    public void resignChessGame(String authToken, int gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException e) {
            System.err.println("WebSocket send/close failed: " + e.getMessage());
        }
    }

    public void leaveChessGame(String authToken, int gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException e) {
            System.err.println("WebSocket send/close failed: " + e.getMessage());
        }
    }

}
