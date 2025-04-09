package websocket.messages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    private ChessGame game;

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public LoadGame(ServerMessageType type, ChessGame chessGame) {
        super(type);
        setGame(chessGame);
    }
}
