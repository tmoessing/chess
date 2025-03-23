package ui;

import server.ServerFacade;

public class InGameClient implements Client {
    private final ServerFacade server;
    private final String serverURL;
    private ChessBoardBuilder chessBoardBuilder;

    InGameClient(String serverURL, ChessBoardBuilder chessBoardBuilder) {
        this.serverURL = serverURL;
        this.chessBoardBuilder = chessBoardBuilder;
        this.server = new ServerFacade(serverURL);

        chessBoardBuilder.run();
    }

    @Override
    public String eval(String command) {

        return "";
    }

    @Override
    public String help() {
        return "Help Message";
    }
}
