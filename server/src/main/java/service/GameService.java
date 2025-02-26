package service;

import dataaccess.GameDAO;

public class GameService extends Service {

    public GameService() {
       super();
    }

    public void clearGames() {
        gameDataAccess.clearGames();
    }
}
