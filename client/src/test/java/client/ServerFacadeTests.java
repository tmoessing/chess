package client;

import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
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

    @BeforeEach
    public void setup() {
        // Clear Databases
        SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        SQLUserDAO sqlUserDAO = new SQLUserDAO();

        sqlUserDAO.clearAllUsers();
        sqlGameDAO.clearGames();
        sqlAuthDAO.clearAuthData();
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

    @Test
    public void loginSuccess() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        facade.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        assertDoesNotThrow( () -> facade.login(loginRequest));
    }

    @Test
    public void loginFailure() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        facade.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password1");
        assertThrows(ResponseException.class, () -> facade.login(loginRequest));
    }

    @Test
    public void createGameSuccess() {
        CreateGameRequest createGameRequest = new CreateGameRequest("game_name");
        assertDoesNotThrow( () -> facade.createGame(createGameRequest));
    }

    @Test
    public void createGameFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest("game_name");
        assertThrows(ResponseException.class, () -> facade.createGame(createGameRequest));
    }
}
