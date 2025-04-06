package websocket.messages;

public class LoadGame extends ServerMessage {
    private String chessGame;

    public LoadGame(ServerMessageType type, String chessGame) {
        super(type);
        this.chessGame = chessGame;
    }
}
