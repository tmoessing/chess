package client;

import java.util.Arrays;
import chess.ChessGame;
import exception.ResponseException;
import model.*;
import ui.Repl;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

public class PostLoginClient implements Client {
    private final ServerFacade server;
    private final String serverURL;

    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;

    PostLoginClient(String serverURL, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        this.notificationHandler = notificationHandler;
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
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
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

    public String logout() {
        Repl.client = new PreLoginClient(this.serverURL, notificationHandler);
        return "Thanks for Playing";
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            String gameName = String.join(" ", params);
            CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
            server.createGame(createGameRequest);
            return String.format("Created Game: %s", gameName);
        } else {
            return "Error: Not Enough Information";
        }
    }

    public String listGames() throws ResponseException {
        ListGamesResult listGamesResult = server.listGames();
        System.out.print("Games:\n");
        for (int gameCounter = 0; gameCounter < listGamesResult.games().size(); gameCounter++) {
            GameRecord game = listGamesResult.games().get(gameCounter);
            System.out.print(game.gameName()+ "(" + (gameCounter+1) + ")" + " - (");
            if (game.whiteUsername() != null) {
                System.out.print("White: " + game.whiteUsername() + " ");
            } else {
                System.out.print("White:    ");
            }
            if (game.blackUsername() != null) {
                System.out.print("Black: " + game.blackUsername() + " )\n");
            } else {
                System.out.print("Black:    )\n\n");
            }
        }
        return "";
    }

    public String joinGame(String... params) throws ResponseException {
        int clientGameID;
        try {
            clientGameID = Integer.parseInt(params[0]) - 1;
        } catch (NumberFormatException ex) {
            return "Please enter a numeric number";
        }

        String colorString = params[1].toUpperCase();
        ChessGame.TeamColor userColor;

        if (colorString.equals("WHITE")) {
            userColor = ChessGame.TeamColor.WHITE;
        } else if (colorString.equals("BLACK")) {
            userColor = ChessGame.TeamColor.BLACK;
        } else {
            return "Invalid Color";
        }

        Repl.userPerspectiveColor = userColor;

        int gameID;
        try {
            ListGamesResult listGamesResult = server.listGames();
            gameID = listGamesResult.games().get(clientGameID).gameID();
        } catch (IndexOutOfBoundsException ex) {
            return "Invalid GameID";
        }

        JoinGameRequest joinGameRequest = new JoinGameRequest(colorString, gameID);
        server.joinGame(joinGameRequest);

        ws = new WebSocketFacade(serverURL, notificationHandler);
        ws.enterChessGame(ServerFacade.getAuthToken(), gameID);

        Repl.client = new InGameClient(serverURL, notificationHandler, gameID);
        return "";
    }

    public String observeGame(String... params) throws ResponseException {
        int clientGameID;

        try {
            clientGameID = Integer.parseInt(params[0]) - 1;
        } catch (NumberFormatException ex) {
            return "Please enter a numeric number";
        }

        ListGamesResult listGamesResult = server.listGames();

        int gameID;
        try {
            gameID = listGamesResult.games().get(clientGameID).gameID();
        } catch (IndexOutOfBoundsException ex) {
            return "Invalid GameID";
        }

        Repl.userPerspectiveColor = ChessGame.TeamColor.WHITE;

        ws = new WebSocketFacade(serverURL, notificationHandler);
        ws.enterChessGame(ServerFacade.getAuthToken(), gameID);

        Repl.client = new ObserveClient(serverURL, notificationHandler, gameID);
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
