package dataaccess;

import model.GameRecord;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() {
        String[] createGamesTable = {
        """
         CREATE TABLE IF NOT EXISTS games (
         `gameID` INT NOT NULL UNIQUE PRIMARY KEY,
         `gameName` VARCHAR(255) NOT NULL UNIQUE,
         `whiteUsername` VARCHAR(255),
         `blackUsername` VARCHAR(255),
         `ChessGameJSON` TEXT NOT NULL
         ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
         """
     };
     DatabaseManager.configureDatabase(createGamesTable);
 }

 public int createGameID() {
     return 0;
 }

 public boolean isGameExistent(int gameID) {
     return false;
 }

 public boolean isGameJoinable(int gameID, String playerColor) {
     return false;
 }

 public void createGame(int gameID, String gameName) {

 }

 public void joinGame(int gameID, String playerColor, String username) {

 }

 public ArrayList<GameRecord> listAllGames() {
     return null;
 }

 public void clearGames() {

 }

 private final String[] createStatements = {

 };
}
