import ui.Repl;

public class Main {
    static String serverUrl = "http://localhost:8080";
    public static void main(String[] args) {
        new Repl(serverUrl).run();
    }
}