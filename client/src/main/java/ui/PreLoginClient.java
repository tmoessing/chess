package ui;

import server.ServerFacade;
import java.util.Arrays;

public class PreLoginClient {
    private final ServerFacade server;
    private final String serverUrl;

    PreLoginClient() {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "login" -> login(params);
            case "register" -> register();
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String login(String... params) {
        if (params.length >= 1) {
           String username = String.join("", params);
           return String.format("Logged in as %s", username);
        }
        return "Error: Logging In";
    }

    public String register() {
        return "";
    }

    public String help() {
        return "Why do you need help?";
    }

}
