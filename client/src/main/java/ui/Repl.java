package ui;

import client.Client;
import client.PreLoginClient;
import websocket.NotificationHandler;
import websocket.messages.Notification;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    public static Client client;
    public static String quitingMessage = "Quiting: Thanks for playing!";

    public Repl(String serverURL) {
        client = new PreLoginClient(serverURL);
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

    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.getMessage());
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_BG_COLOR + ">>> ");
    }
}
