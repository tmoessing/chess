package websocket.messages;

public class LoadGame extends ServerMessage {
    private String game ;

    public LoadGame(ServerMessageType type, String chessBoardString) {
        super(type);
        this.game = chessBoardString;
    }

    public String toString() {
        return game;
    }
}
