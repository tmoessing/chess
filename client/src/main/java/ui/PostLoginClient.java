package ui;

import exception.ResponseException;
import model.CreateGameRequest;
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
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                case "clear" -> clear();
                case "help" -> help();
                case "quit" -> Repl.quitingMessage;
                default -> "Invalid instruction";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logout() {
        Repl.client = new PreLoginClient(this.serverURL);
        return "Thanks for Playing";
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length != 0) {
            String gameName = params[0];
            CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
            server.createGame(createGameRequest);
            return String.format("Created game %s", gameName);
        } else {
            return  "Error Handling Given Command. Write 'help' for Help!";
        }
    }

    public String listGames() {
        return  "";
    }

    public String playGame(String... params) {
        String color = "BLACK";
        Repl.client = new InGameClient(serverURL, new ChessBoardBuilder(color));
        return "";
    }

    public String observeGame(String... params) {
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
