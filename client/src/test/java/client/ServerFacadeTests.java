package client;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerSuccess() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        assertDoesNotThrow( () -> facade.register(registerRequest));
    }

    @Test
    public void registerFailure() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        facade.register(registerRequest);
        assertThrows(ResponseException.class, () -> facade.register(registerRequest));
    }
}
