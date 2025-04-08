package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, List<Connection>> allConnections = new ConcurrentHashMap<>();

    public void add(String username, int gameID, Session session) {
        var connection = new Connection(username, session);
        List<Connection> gameConnections = allConnections.get(gameID);
        if (gameConnections == null) {
            gameConnections = new ArrayList<>();
        }
        gameConnections.add(connection);
        allConnections.put(gameID, gameConnections);
    }

    public void leave(int gameID, String username) {
//        var removeList = new ArrayList<Connection>();
//
//        for (var c : connections.values()) {
//            if (c.session.isOpen()) {
//                if (c.username.equals(username)) {
//                    removeList.add(c);
//                }
//            } else {
//                removeList.add(c);
//            }
//        }

//        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            connections.get(gameID;
//        }
    }

    public void endGame(int gameID) {
        allConnections.remove(gameID);
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var gameConnection : allConnections.values()) {
            for (var connection : gameConnection) {
                if (connection.session.isOpen()) {
                    if (!connection.username.equals(excludeVisitorName)) {
                        connection.send(new Gson().toJson(notification));
                    }
                } else {
                    removeList.add(connection);
                }
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            allConnections.remove(c);
        }
    }

    public void broadcastRoot(String rootUsername, ServerMessage notification) throws IOException {
        for (var gameConnection : allConnections.values()) {
            for (var connection : gameConnection) {
                if (connection.session.isOpen()) {
                    if (connection.username.equals(rootUsername)) {
                        connection.send(new Gson().toJson(notification));
                    }
                }
                }
        }
    }
}