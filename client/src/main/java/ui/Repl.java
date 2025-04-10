package ui;

import chess.ChessGame;
import client.Client;
import client.PreLoginClient;
import com.google.gson.Gson;
import websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    public static Client client;
    public static String quitingMessage = "Quiting: Thanks for playing!";
    public static ChessGame.TeamColor userPerspectiveColor;

    public Repl(String serverURL) {
        client = new PreLoginClient(serverURL, this);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_WHITE + "♔ CHESS \nWelcome!");
        System.out.print(client.help());
        System.out.print("\n\nType your command");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals(quitingMessage)) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_WHITE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    public void notify(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        ServerMessage.ServerMessageType type = serverMessage.getServerMessageType();
        switch (type) {
            case NOTIFICATION ->  displayNotification(new Gson().fromJson(message, Notification.class));
            case ERROR ->  displayError(new Gson().fromJson(message, ErrorMessage.class));
            case LOAD_GAME -> loadGame(new Gson().fromJson(message, LoadGame.class));
        }
    }

    private void displayNotification(Notification notification) {
        System.out.println(SET_TEXT_COLOR_BLUE + notification.getMessage());
        printPrompt();
    }

    private void displayError(ErrorMessage errorMessage) {
        System.out.println(SET_TEXT_COLOR_RED + errorMessage.getErrorMessage());
        printPrompt();
    }

    private void loadGame(LoadGame message) {
        ChessBoardBuilder chessBoardBuilder = new ChessBoardBuilder(message.getGame(), userPerspectiveColor);
        chessBoardBuilder.run();
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_BG_COLOR + RESET_TEXT_COLOR + ">>> ");
    }
}
