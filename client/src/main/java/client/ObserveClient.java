package client;

import ui.ChessBoardBuilder;
import ui.Repl;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

import static ui.Repl.chessBoardBuilder;

public class ObserveClient implements Client {
    private final ServerFacade server;
    private final String serverURL;
    private int gameID;

    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;

    ObserveClient(String serverURL, NotificationHandler notificationHandler, int gameID) {
        this.serverURL = serverURL;
        this.server = new ServerFacade(serverURL);

        this.notificationHandler = notificationHandler;

        this.gameID = gameID;
    }

    @Override
    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "redraw" -> redraw();
            case "leave" -> leave();
            case "highlight" -> highlight(params);
            case "help" -> help();
            case "quit" -> Repl.quitingMessage;
            default -> "Invalid instructions";
        };
    }

    public String leave() {
        ws = new WebSocketFacade(serverURL, notificationHandler);
        ws.leaveChessGame(ServerFacade.getAuthToken(), gameID);
        Repl.client = new PostLoginClient(this.serverURL, notificationHandler);

        Repl.userPerspectiveColor = null;
        return "Welcome back to the home page!";
    }

    public String redraw() {
        System.out.print("Redrawing Board...\n");
        chessBoardBuilder.run();
        return "";
    }

    public String highlight(String... params) {
        if (params.length == 1) {
            String pos = params[0];
            chessBoardBuilder.highlightMoves(pos, Repl.userPerspectiveColor);
            return "";
        }
        return "Invalid use of highlight";
    }

    public String help() {
        return  "Instructions:" +
                "\n leave - leave the game being observed" +
                "\n help - get help with possible commands" +
                "\n quit - quit the application" +
                "\n highlight <row,col> - highlight possible moves" +
                "\n redraw - redraw board";
    }
}
