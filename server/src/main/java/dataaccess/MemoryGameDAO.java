package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    private static final ArrayList<GameData> gameList = new ArrayList<>();

    public void createGame(String gameData) throws DataAccessException {

    }

    public void findGameViaGameID(int gameID) throws DataAccessException {

    }

    public void joinGame(String username) throws DataAccessException {

    }

    public void getAllGames() throws DataAccessException {

    }

    public void clearGames() {
        gameList.clear();
    }
}
