package websocket.messages;

public class Notification extends ServerMessage {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum Type {
        ARRIVAL,
        NOISE,
        DEPARTURE,
    }

    public Notification(ServerMessageType type, String notificationMessage) {
        super(type);
        setMessage(notificationMessage);
    }

    public String toString() {
        return message;
    }
}