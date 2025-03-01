package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameRecord;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private static final ArrayList<GameData> gameList = new ArrayList<>();

    public int createGameID(){
        return gameList.size() + 1;
    }

    public boolean isGameExistent(int gameID) {
        for (GameData gameData : gameList) {
            if (gameData.gameID() == gameID) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameJoinable(int gameID, String playerColor) {
        for (GameData gameData : gameList) {
            if (gameData.gameID() == gameID) {
                if ((Objects.equals(playerColor, "BLACK") && gameData.blackUsername() == null || (Objects.equals(playerColor, "WHITE") && gameData.whiteUsername() == null))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void createGame(int gameID, String gameName) {
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        gameList.add(gameData);
    }

    public void joinGame(int gameID, String playerColor, String username) {
        GameData newGameData = null;
        for (GameData gameData : gameList) {
            if (gameData.gameID() == gameID) {
                String whiteUsername;
                String blackUsername;
                if (Objects.equals(playerColor, "WHITE")) {
                    whiteUsername = username;
                    blackUsername = gameData.blackUsername();
                } else {
                    whiteUsername = gameData.whiteUsername();
                    blackUsername = username;
                }
                newGameData = new GameData(gameData.gameID(), whiteUsername, blackUsername, gameData.gameName(), gameData.game());
                gameList.remove(gameData);
                gameList.add(newGameData);
                return;
            }
        }
    }

    public ArrayList<GameRecord> listAllGames() {
        ArrayList<GameRecord> games = new ArrayList<>();
        for (GameData gameData : gameList) {
            GameRecord gameRecord = new GameRecord(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
            games.add(gameRecord);
        }
        return games;
    }

    public void clearGames() {
        ArrayList<GameData> gameList2 = gameList;
        gameList.clear();
    }
}
