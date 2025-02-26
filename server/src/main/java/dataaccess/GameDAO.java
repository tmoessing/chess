package dataaccess;

public interface GameDAO {
    void createGame(String gameData) throws DataAccessException;

    void findGameViaGameID(int gameID) throws DataAccessException;

    void joinGame(String username) throws DataAccessException;

    void getAllGames() throws DataAccessException;

    void clearGames();
}
