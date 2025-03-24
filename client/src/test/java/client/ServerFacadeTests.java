package client;

import model.*;
import dataaccess.*;
import server.Server;

import org.junit.jupiter.api.*;
import exception.ResponseException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    private final RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
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
        assertDoesNotThrow( () -> facade.register(registerRequest));
    }

    @Test
    public void registerFailure() throws ResponseException {
        facade.register(registerRequest);
        assertThrows(ResponseException.class, () -> facade.register(registerRequest));
    }

    @Test
    public void loginSuccess() throws ResponseException {
        facade.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password");
        assertDoesNotThrow( () -> facade.login(loginRequest));
    }

    @Test
    public void loginFailure() throws ResponseException {
        facade.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("username", "password1");
        assertThrows(ResponseException.class, () -> facade.login(loginRequest));
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        facade.register(registerRequest);
        assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void logoutFailure() throws ResponseException {
        assertThrows(ResponseException.class, () -> facade.logout());
    }

    @Test
    public void clearSuccess() throws ResponseException {
        facade.register(registerRequest);
        assertDoesNotThrow(() -> facade.clear());
    }

    @Test
    public void listGamesSuccess() throws ResponseException {
        facade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("game_name");
        facade.createGame(createGameRequest);
        assertDoesNotThrow(() -> facade.listGames());
    }

    @Test
    public void listGamesFailure() {
        assertThrows(ResponseException.class, () -> facade.listGames());
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        facade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("game_name");
        assertDoesNotThrow( () -> facade.createGame(createGameRequest));
    }

    @Test
    public void createGameFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest("game_name");
        assertThrows(ResponseException.class, () -> facade.createGame(createGameRequest));
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        facade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("game_name");
        assertDoesNotThrow( () -> facade.createGame(createGameRequest));
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
        assertDoesNotThrow( () -> facade.joinGame(joinGameRequest));
    }

    @Test
    public void joinGameFailure() throws ResponseException {
        facade.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("game_name");
        assertDoesNotThrow( () -> facade.createGame(createGameRequest));
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 1);
        facade.joinGame(joinGameRequest);
        assertThrows(ResponseException.class,  () -> facade.joinGame(joinGameRequest));
    }

}
