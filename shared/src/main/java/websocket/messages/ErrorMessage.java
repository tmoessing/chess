package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorMessage(ServerMessageType type, String message) {
        super(type);
        setErrorMessage(message);
    }
}
