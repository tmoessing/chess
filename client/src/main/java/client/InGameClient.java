package client;

import chess.ChessGame;
import ui.ChessBoardBuilder;
import ui.Repl;

import java.util.Arrays;

public class InGameClient implements Client {
    private final ServerFacade server;
    private final String serverURL;
    private final ChessGame.TeamColor playerColor;
    private ChessBoardBuilder chessBoardBuilder;


    InGameClient(String serverURL, ChessBoardBuilder chessBoardBuilder) {
        this.serverURL = serverURL;
        this.chessBoardBuilder = chessBoardBuilder;
        this.server = new ServerFacade(serverURL);

        this.playerColor = chessBoardBuilder.color;

        chessBoardBuilder.run();
    }

    @Override
    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "redraw" -> redraw();
            case "resign" -> resign();
            case "move" -> move(params);
            case "highlight" -> highlight(params);
            case "leave" -> leave();
            case "logout" -> logout();
            case "help" -> help();
            case "quit" -> Repl.quitingMessage;
            default -> "Invalid instruction";
        };
    }

    public String redraw() {
        System.out.print("Redrawing Board...\n");
        chessBoardBuilder.run();
        return "";
    }

    public String resign() {
        return "";
    }

    public String move(String... params) {
        if ((chessBoardBuilder.chessGame.getTeamTurn()).equals(this.playerColor)) {
            return "";
        }
        return "Not your Turn";
    }


    public String highlight(String... params) {
        if (params.length == 1) {
            String pos = params[0];
            return chessBoardBuilder.highlightMoves(pos, this.playerColor);
        }
        return "Invalid use of highlight";
    }

    public String leave() {
        Repl.client = new PostLoginClient(this.serverURL);
        return "Welcome back to the home page!";
    }

    public String logout() {
        Repl.client = new PreLoginClient(this.serverURL);
        return "Thanks for Playing!";
    }

    public String help() {
        return "Game Help Message (help/quit)";
    }
}
