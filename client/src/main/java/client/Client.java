package client;

import websocket.NotificationHandler;

public interface Client {
    String eval(String command);
    String help();
}
