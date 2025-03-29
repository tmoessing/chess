package client;

import ui.ChessBoardBuilder;
import ui.Repl;

import java.util.Arrays;

public class ObserveClient implements Client {
    private final ServerFacade server;
    private final String serverURL;
    private ChessBoardBuilder chessBoardBuilder;

    ObserveClient(String serverURL, ChessBoardBuilder chessBoardBuilder) {
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
            case "leave" -> leave();
            case "help" -> help();
            case "logout" -> logout();
            case "quit" -> Repl.quitingMessage;
            default -> "Invalid instructions";
        };
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
        return "Observe Help Message (help/quit)";
    }
}
