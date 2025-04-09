package client;

import chess.ChessGame;
import chess.ChessMove;
import ui.ChessBoardBuilder;
import ui.Repl;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

public class InGameClient implements Client {
    private WebSocketFacade ws;
    private final ServerFacade server;
    private final NotificationHandler notificationHandler;
    private final String serverURL;
    private ChessGame.TeamColor playerColor;
    private int gameID;
    private ChessBoardBuilder chessBoardBuilder;


    InGameClient(String serverURL, NotificationHandler notificationHandler, ChessBoardBuilder chessBoardBuilder, int gameID) {
        this.serverURL = serverURL;
        this.server = new ServerFacade(serverURL);
        this.notificationHandler = notificationHandler;

        this.gameID = gameID;
        this.playerColor = chessBoardBuilder.color;
        this.chessBoardBuilder = chessBoardBuilder;

        chessBoardBuilder.run();
    }

    @Override
    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "redraw" -> redraw();
            case "resign" -> resign();
            case "move" -> move(params);
            case "highlight" -> highlight(params);
            case "leave" -> leave();
            case "logout" -> logout();
            case "help" -> help();
            case "quit" -> Repl.quitingMessage;
            default -> "Invalid instruction";
        };
    }

    public String redraw() {
        System.out.print("Redrawing Board...\n");
        chessBoardBuilder.run();
        return "";
    }

    public String resign() {
        ws = new WebSocketFacade(serverURL, notificationHandler);
        ws.resignChessGame(ServerFacade.getAuthToken(), gameID);
        return "";
    }

    public String move(String... params) {
        if ((chessBoardBuilder.chessGame.getTeamTurn()).equals(this.playerColor)) {
            return "";
        } else if (params.length == 2) {
            String startPos = params[0];
            String endPos = params[1];
            ChessMove chessMove = chessBoardBuilder.makeMove(gameID, startPos, endPos, this.playerColor);
            if (chessMove != null) {
                ws = new WebSocketFacade(serverURL, notificationHandler);
                ws.makeMove(ServerFacade.getAuthToken(), gameID, chessMove);
            }
            return "";
        }
        return "Not your Turn";
    }


    public String highlight(String... params) {
        if (params.length == 1) {
            String pos = params[0];
            chessBoardBuilder.highlightMoves(pos, this.playerColor);
            return "";
        }
        return "Invalid use of highlight";
    }

    public String leave() {
        ws = new WebSocketFacade(serverURL, notificationHandler);
        ws.leaveChessGame(ServerFacade.getAuthToken(), gameID);
        Repl.client = new PostLoginClient(this.serverURL, notificationHandler);
        return "Welcome back to the home page!";
    }

    public String logout() {
        Repl.client = new PreLoginClient(this.serverURL, notificationHandler);
        return "Thanks for Playing!";
    }

    public String help() {
        return "Game Help Message (help/quit)";
    }
}
