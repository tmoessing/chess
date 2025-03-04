package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.JoinGameRequest;
import model.ListGamesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService gameService;
    private MemoryAuthDAO memoryAuthDAO;
    private MemoryGameDAO memoryGameDAO;

    @BeforeEach
    void setup() {
        gameService = new GameService();
        memoryGameDAO = new MemoryGameDAO();
        memoryAuthDAO = new MemoryAuthDAO();
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        String authToken = "good_auth_token";
        memoryAuthDAO.addAuthData(authToken, "username");
        CreateGameResult observedCreateGameResult = gameService.createGame(createGameRequestTest, authToken);
        assertEquals(new CreateGameResult(1), observedCreateGameResult);
    }

    @Test
    void createGameUnauthorized() throws DataAccessException {
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        String authToken = "bad_auth_token";
        Exception exception = assertThrows(DataAccessException.class,
                () -> {gameService.listGames(authToken);});

        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        memoryGameDAO.clearGames();

        // Authenticate User
        String authToken = "good_auth_token";
        String username = "username";
        memoryAuthDAO.addAuthData(authToken, username);

        // Create Game
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        CreateGameResult observedCreateGameResult = gameService.createGame(createGameRequestTest, authToken);

        // Join Game
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", observedCreateGameResult.gameID());
        gameService.joinGame(joinGameRequest, authToken);
        assertEquals(username, memoryGameDAO.listAllGames().getFirst().whiteUsername());
    }

    @Test
    void joinGameUnauthorized() throws DataAccessException {
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        String authToken = "bad_auth_token";
        Exception exception = assertThrows(DataAccessException.class,
                () -> {gameService.listGames(authToken);});

        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void joinGameBadRequest() throws DataAccessException {
        // Authenticate User
        String authToken = "good_auth_token";
        String username = "username";
        memoryAuthDAO.addAuthData(authToken, username);

        // Create Game
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        CreateGameResult observedCreateGameResult = gameService.createGame(createGameRequestTest, authToken);

        // Join Game
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 3);

        Exception exception = assertThrows(DataAccessException.class,
                () -> {gameService.joinGame(joinGameRequest, authToken);});

        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void joinGameAlreadyTaken() throws DataAccessException {
        // Authenticate User
        String authToken = "good_auth_token";
        String username = "username";
        memoryAuthDAO.addAuthData(authToken, username);

        // Create Game
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        CreateGameResult observedCreateGameResult = gameService.createGame(createGameRequestTest, authToken);

        // Join Game
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", observedCreateGameResult.gameID());
        gameService.joinGame(joinGameRequest, authToken);

        Exception exception = assertThrows(DataAccessException.class,
                () -> {gameService.joinGame(joinGameRequest, authToken);});

        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        memoryGameDAO.clearGames();

        // Authenticate User
        String authToken = "good_auth_token";
        memoryAuthDAO.addAuthData(authToken, "username");

        // Create Game
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        CreateGameResult observedCreateGameResult = gameService.createGame(createGameRequestTest, authToken);

        // List Games
        ListGamesResult listGamesResult = gameService.listGames(authToken);
        assertEquals(1, listGamesResult.games().size());
    }

    @Test
    void listGamesUnauthorized() throws DataAccessException {
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        String authToken = "bad_auth_token";
        Exception exception = assertThrows(DataAccessException.class,
                () -> {gameService.listGames(authToken);});

        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void clearGames() throws DataAccessException {
        // Authenticate User
        String authToken = "good_auth_token";
        memoryAuthDAO.addAuthData(authToken, "username");

        // Create Game
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        CreateGameResult observedCreateGameResult = gameService.createGame(createGameRequestTest, authToken);

        // Clear Game
        gameService.clearGames();

        assertEquals(0, memoryGameDAO.listAllGames().size());
    }
}