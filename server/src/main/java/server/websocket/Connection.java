package server.websocket;

import com.mysql.cj.protocol.x.XMessage;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String username;
    public Session session;

    public Connection(String username, Session session) {
        this.username = username;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
        if (msg.equals("yolo")) {
            sendError(session, "");
        }
    }

    public void sendError(Session thisSession, String msg) throws IOException {
        thisSession.getRemote().sendString(msg);
    }
}