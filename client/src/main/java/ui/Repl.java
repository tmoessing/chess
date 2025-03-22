package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    public static Client client;

    public Repl(String serverURL) {
        client = new PreLoginClient(serverURL);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_WHITE + "♔ CHESS \nWelcome!");
        System.out.print(client.help());
        System.out.print("\nType your command");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
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

    private void printPrompt() {
        System.out.print("\n" + RESET_BG_COLOR + ">>> ");
    }
}
