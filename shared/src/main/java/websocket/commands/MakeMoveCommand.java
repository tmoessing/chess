package websocket.commands;

import chess.ChessMove;
import com.google.gson.Gson;

public class MakeMoveCommand extends UserGameCommand {
//    private ChessMove chessMove;
    private ChessMove move;


    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, ChessMove chessMove) {
        super(commandType, authToken, gameID);
        this.move = chessMove;
    }

}
