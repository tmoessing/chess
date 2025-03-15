package ui;

import java.util.Arrays;

public class PostLoginClient {
    PostLoginClient() {

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
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String logout() {
        return "";
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
        return "Why do you need help?";
    }

}
