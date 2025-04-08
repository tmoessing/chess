package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
//    private ChessMove chessMove;
    private String chessGameString;


    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, String chessGameString) {
        super(commandType, authToken, gameID);
        this.chessGameString = chessGameString;
    }

}
