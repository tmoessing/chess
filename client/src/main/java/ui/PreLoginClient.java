package ui;

import exception.ResponseException;
import model.*;
import server.ServerFacade;
import java.util.Arrays;

public class PreLoginClient {
    private final ServerFacade server;
    private final String serverUrl;

    PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register();
                case "quit" -> "Thanks for playing";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
           String username = params[0];
           String password = params[1];
           LoginRequest loginRequest = new LoginRequest(username, password);
           server.login(loginRequest);
           return String.format("Logged in as %s", username);
        }
        return "Error: Not Enough Information";
    }

    public String register() {
        return "";
    }

    public String help() {
        return "Why do you need help?";
    }

}
