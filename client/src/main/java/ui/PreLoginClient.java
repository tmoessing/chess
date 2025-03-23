package ui;

import exception.ResponseException;
import model.*;
import server.ServerFacade;
import java.util.Arrays;

public class PreLoginClient implements Client {
    private final ServerFacade server;
    private final String serverURL;

    PreLoginClient(String serverURL) {
        server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "clear" -> clear();
                case "help" -> help();
                case "quit" -> Repl.quitingMessage;
                default -> "Invalid instruction";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        } catch (ArrayIndexOutOfBoundsException ex) {
            return "Error: Not Enough Information";
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
           String username = params[0];
           String password = params[1];
           LoginRequest loginRequest = new LoginRequest(username, password);
           server.login(loginRequest);
           System.out.printf("Logging in... \nWelcome %s!", username);
           Repl.client = new PostLoginClient(this.serverURL);
           return "";
        } else if (params.length > 2) {
            return "Error: Too Much Information";
        } else {
            return "Error: Not Enough Information";
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest registerRequest  = new RegisterRequest(username, password, email);
            server.register(registerRequest);
            System.out.printf("Registering... \nWelcome %s, here are some of the new ", username);
            Repl.client = new PostLoginClient(this.serverURL);
            return Repl.client.help();
        } else if (params.length > 3) {
            return "Error: Too Much Information";
        } else {
            return "Error: Not Enough Information";
        }
    }

    public String help() {
        return "Instructions: \nregister <USERNAME> <PASSWORD> <EMAIL> - to create an account" +
                "\nlogin <USERNAME> <PASSWORD> - to play chess" +
                "\nquit - playing chess" +
                "\nhelp - with possible commands";
    }

    public String clear() throws ResponseException {
        server.clear();
        return "Clearing...";
    }
}
