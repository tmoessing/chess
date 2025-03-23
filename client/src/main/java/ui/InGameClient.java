package ui;

import exception.ResponseException;
import server.ServerFacade;

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
            case "help" -> help();
            case "quit" -> Repl.quitingMessage;
            default -> "Invalid instruction";
        };
    }

    @Override
    public String help() {
        return "Help Message (help/quit)";
    }
}
