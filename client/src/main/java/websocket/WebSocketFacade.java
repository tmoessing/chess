package websocket;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public class WebSocketFacade extends Endpoint {
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        //Endpoint requires this method, but you don't have to do anything
    }
}
