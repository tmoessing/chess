package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;

public class GameService extends Service {

    public GameService() {
       super();
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException {
        if (!authDataAccess.isAuthTokenExistent(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }

        int gameID = gameDataAccess.createGameID();
        gameDataAccess.createGame(gameID, createGameRequest.gameName());

        return new CreateGameResult(gameID);
    }

    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws DataAccessException {
        if (!authDataAccess.isAuthTokenExistent(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        } else if (!gameDataAccess.isGameExistent(joinGameRequest.gameID())) {
            throw new DataAccessException("Error: bad request");
        } else if (!gameDataAccess.isGameJoinable(joinGameRequest.gameID(), joinGameRequest.playerColor())) {
            throw new DataAccessException("Error: already taken");
        }

        String username = authDataAccess.getUsernameViaAuthToken(authToken);
        gameDataAccess.joinGame(joinGameRequest.gameID(), joinGameRequest.playerColor(), username);

    }

    public ListGamesResult listGames(String authToken) throws DataAccessException {
        if (!authDataAccess.isAuthTokenExistent(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }

        return new ListGamesResult(gameDataAccess.listAllGames());
    }

    public void clearGames() {
        gameDataAccess.clearGames();
    }
}
