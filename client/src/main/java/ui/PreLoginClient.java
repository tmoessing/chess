package ui;

import java.util.Arrays;

public class PreLoginClient {

    PreLoginClient() {

    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "login" -> login();
            case "register" -> register();
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String login() {
        return "";
    }

    public String register() {
        return "";
    }

    public String help() {
        return "Why do you need help?";
    }

}
