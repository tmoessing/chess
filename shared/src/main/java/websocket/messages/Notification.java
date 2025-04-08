package websocket.messages;

import com.google.gson.Gson;

public class Notification extends ServerMessage {
    private String message;

    public enum Type {
        ARRIVAL,
        NOISE,
        DEPARTURE,
    }

    public Notification(ServerMessageType type, String notificationMessage) {
        super(type);
        this.message = notificationMessage;
    }

    public String toString() {
        return message;
    }
}