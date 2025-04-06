package websocket.messages;

import com.google.gson.Gson;

public class Notification extends ServerMessage {
    private String notificationMessage;

    public enum Type {
        ARRIVAL,
        NOISE,
        DEPARTURE,
    }

    public Notification(ServerMessageType type, String notificationMessage) {
        super(type);
        this.notificationMessage = notificationMessage;
    }

    public String toString() {
        return notificationMessage;
    }
}