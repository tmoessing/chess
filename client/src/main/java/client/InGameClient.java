package client;

import ui.ChessBoardBuilder;
import ui.Repl;

import java.util.Arrays;

public class InGameClient implements Client {
    private final ServerFacade server;
    private final String serverURL;
    private ChessBoardBuilder chessBoardBuilder;

    InGameClient(String serverURL, ChessBoardBuilder chessBoardBuilder) {
        this.serverURL = serverURL;
        this.chessBoardBuilder = chessBoardBuilder;
        this.server = new ServerFacade(serverURL);

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
            case "highlight" -> highlight();
            case "exit" -> exit();
            case "logout" -> logout();
            case "help" -> help();
            case "quit" -> Repl.quitingMessage;
            default -> "Invalid instruction";
        };
    }

    public String redraw() {
        return "";
    }

    public String resign() {
        return "";
    }

    public String highlight() {
        return "";
    }

    public String exit() {
        Repl.client = new PostLoginClient(this.serverURL);
        return "Welcome back to the home page!";
    }

    public String logout() {
        Repl.client = new PreLoginClient(this.serverURL);
        return "Thanks for Playing!";
    }

    @Override
    public String help() {
        return "Help Message (help/quit)";
    }
}
