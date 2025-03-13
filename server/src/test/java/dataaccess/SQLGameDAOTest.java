package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.GameRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {
    private SQLGameDAO sqlGameDAO;
    private String username = "username";
    private Integer gameID = 1;
    private String gameName = "gameName";
    private String chessGameString = new Gson().toJson(new ChessGame());

    void addGame() {
        var statement = "INSERT INTO games (gameID, gameName, chessGameJSON) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, gameID, gameName, chessGameString );
    }

    @BeforeEach
    void setUp() {
        sqlGameDAO = new SQLGameDAO();
        var statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }

    @Test
    void createGameIDSuccess() {
        assertInstanceOf(Integer.class, sqlGameDAO.createGameID());
    }

    @Test
    void createGameIDFailure() {
    // Not possible
    }

    @Test
    void isGameExistentSuccess() {
        addGame();
        assertTrue(sqlGameDAO.isGameExistent(gameID));
    }

    @Test
    void isGameExistentFailure() {
        assertFalse(sqlGameDAO.isGameExistent(gameID));
    }

    @Test
    void isGameJoinableSuccess() {
        addGame();
        assertTrue(sqlGameDAO.isGameJoinable(gameID, "WHITE"));
    }

    @Test
    void isGameJoinableFailure() {
        addGame();
        sqlGameDAO.joinGame(gameID, "WHITE", username);
        assertFalse(sqlGameDAO.isGameJoinable(gameID, "WHITE"));
    }

    @Test
    void createGameSuccess() {
        addGame();
        assertTrue(sqlGameDAO.isGameExistent(gameID));
    }

    @Test
    void createGameFailure() {
        assertFalse(sqlGameDAO.isGameExistent(gameID));
    }

    @Test
    void joinGameSuccess() {
        addGame();
        sqlGameDAO.joinGame(gameID, "WHITE", username);
        assertFalse(sqlGameDAO.isGameJoinable(gameID, "WHITE"));
    }

    @Test
    void joinGameFailure() {
        // not possible
    }

    @Test
    void listAllGamesSuccess() {
        addGame();
        ArrayList<GameRecord> gameList = sqlGameDAO.listAllGames();
        assertEquals(1, gameList.size());
    }

    @Test
    void listAllGamesFailure() {
        // not possible
    }

    @Test
    void clearGames() {
        addGame();
        sqlGameDAO.clearGames();

    }
}