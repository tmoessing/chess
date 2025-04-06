package websocket.messages;

public class LoadGame extends ServerMessage {
    private String chessBoardString;

    public LoadGame(ServerMessageType type, String chessBoardString) {
        super(type);
        this.chessBoardString = chessBoardString;
    }

    public String toString() {
        return chessBoardString;
    }
}
