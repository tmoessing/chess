package ui;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class PostLoginClient implements Client {
    private final ServerFacade server;
    private final String serverURL;

    PostLoginClient(String serverURL) {
        server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "logout" -> logout();
            case "create game" -> createGame();
            case "list games" -> listGames();
            case "play game" -> playGame();
            case "observe game" -> observeGame();
            case "quit" -> Repl.quitingMessage;
            default -> help();
        };
    }

    public String logout() {
        Repl.client = new PreLoginClient(this.serverURL);
        return "bye";
    }

    public String createGame() {
        return  "";
    }

    public String listGames() {
        return  "";
    }

    public String playGame() {
        return "";
    }

    public String observeGame() {
        return "";
    }

    public String help() {
        return "Instructions:" +
                "\n create <NAME> - a game" +
                "\n list - games" +
                "\n join <ID> [WHITE|BLACK] - a game" +
                "\n observe <ID> - a game" +
                "\n logout - when you are done" +
                "\n quit - playing chess" +
                "\n help - with possible commands";
    }

    public String clear() throws ResponseException {
        server.clear();
        return "Clearing...";
    }

}
