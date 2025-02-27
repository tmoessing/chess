package dataaccess;

import model.GameData;

public interface GameDAO {

    int createGameID();

    boolean isGameExistent(int gameID);

    boolean isGameJoinable(int gameID, String playerColor);

    void createGame(int gameID, String gameName);

    void joinGame(int gameID, String playerColor, String username);

    void getAllGames() throws DataAccessException;

    void clearGames();
}
