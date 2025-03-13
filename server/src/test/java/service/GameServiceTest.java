package service;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.JoinGameRequest;
import model.ListGamesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameServiceTest {

    private GameService gameService;
    private SQLAuthDAO sqlAuthDAO;
    private SQLGameDAO sqlGameDAO;

    @BeforeEach
    void setup() {
        gameService = new GameService();
        sqlGameDAO = new SQLGameDAO();
        sqlAuthDAO = new SQLAuthDAO();

        sqlAuthDAO.clearAuthData();
        sqlGameDAO.clearGames();
    }

    CreateGameResult createGameResult() throws DataAccessException {
        // Authenticate User
        String authToken = "good_auth_token";
        String username = "username";
        sqlAuthDAO.addAuthData(username, authToken);

        // Create Game
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        return gameService.createGame(createGameRequestTest, authToken);
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        CreateGameResult observedCreateGameResult = createGameResult();
        assertEquals(new CreateGameResult(1), observedCreateGameResult);
    }

    @Test
    void createGameUnauthorized() throws DataAccessException {
        CreateGameRequest createGameRequestTest = new CreateGameRequest("gameNameTest");
        String authToken = "bad_auth_token";
        Exception exception = assertThrows(DataAccessException.class,
                () -> {gameService.createGame(createGameRequestTest, authToken);});

        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        CreateGameResult observedCreateGameResult = createGameResult();

        // Join Game
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", observedCreateGameResult.gameID());
        gameService.joinGame(joinGameRequest, "good_auth_token");
        assertEquals("username", sqlGameDAO.listAllGames().getFirst().whiteUsername());
    }

    @Test
    void joinGameUnauthorized() throws DataAccessException {
        CreateGameResult observedCreateGameResult = createGameResult();

        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", observedCreateGameResult.gameID());
        String authToken2 = "bad_auth_token";
        Exception exception = assertThrows(DataAccessException.class,
                () -> {gameService.joinGame(joinGameRequest, authToken2);});

        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void joinGameBadRequest() throws DataAccessException {
        CreateGameResult observedCreateGameResult = createGameResult();

        // Join Game
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 3);

        Exception exception = assertThrows(DataAccessException.class,
                () -> {gameService.joinGame(joinGameRequest, "good_auth_token");});

        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    void joinGameAlreadyTaken() throws DataAccessException {
        CreateGameResult observedCreateGameResult = createGameResult(); // Create Game

        // Join Game
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", observedCreateGameResult.gameID());
        gameService.joinGame(joinGameRequest, "good_auth_token");

        Exception exception = assertThrows(DataAccessException.class,
                () -> {gameService.joinGame(joinGameRequest, "good_auth_token");});

        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        createGameResult();

        // List Games
        ListGamesResult listGamesResult = gameService.listGames("good_auth_token");
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
        createGameResult();

        // Clear Game
        gameService.clearGames();

        assertEquals(0, sqlGameDAO.listAllGames().size());
    }
}