package dataaccess;

import model.GameData;
import model.GameRecord;

import java.util.ArrayList;

public interface GameDAO {

    int createGameID();

    boolean isGameExistent(int gameID);

    boolean isGameJoinable(int gameID, String playerColor);

    void createGame(int gameID, String gameName);

    void joinGame(int gameID, String playerColor, String username);

    ArrayList<GameRecord> listAllGames();

    void clearGames();
}
