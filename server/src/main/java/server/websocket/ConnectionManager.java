package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, int gameID, Session session) {
        var connection = new Connection(username, session);
        connections.put(gameID, connection);
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
        connections.remove(gameID);
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeVisitorName)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }
}