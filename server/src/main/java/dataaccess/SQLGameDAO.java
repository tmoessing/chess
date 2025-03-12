package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameRecord;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

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
    String query = "SELECT COUNT(*) FROM games";
     try (var conn = DatabaseManager.getConnection();
          var ps = conn.prepareStatement(query)) {
         try (var rs = ps.executeQuery()) {
             if (rs.next()) {
                 return rs.getInt("gameID") +1;
             }
         }
     } catch (SQLException | DataAccessException e) {
         System.out.println("Database error: " + e.getMessage());
     }
     return 1;
    }

 public boolean isGameExistent(int gameID) {
     String query = "SELECT 1 FROM games WHERE gameID=? LIMIT 1";

     try (var conn = DatabaseManager.getConnection();
          var ps = conn.prepareStatement(query)) {

         ps.setInt(1, gameID);

         try (var rs = ps.executeQuery()) {
             return rs.next();
         }
     } catch (SQLException | DataAccessException e) {
         System.out.println("Database error: " + e.getMessage());
     }
     return false;
 }

 public boolean isGameJoinable(int gameID, String playerColor) {
     String query = "SELECT whiteUsername, blackUsername FROM games WHERE gameID=? LIMIT 1";

     try (var conn = DatabaseManager.getConnection();
          var ps = conn.prepareStatement(query)) {

         ps.setInt(1, gameID);

         try (var rs = ps.executeQuery()) {
            if (rs.next()) {
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                if (playerColor.equals("WHITE") && whiteUsername == null){
                    return true;
                } else if (playerColor.equals("BLACK") && blackUsername == null) {
                    return true;
                } else {
                    return false;
                }
            }
         }
     } catch (SQLException | DataAccessException e) {
         System.out.println("Database error: " + e.getMessage());
     }
     return false;
    }

 public void createGame(int gameID, String gameName) {
        var statement = "INSERT INTO games (gameID, gameName, ChessGameJSON) VALUES (?, ?, ?)";
        var ChessGameJSON = new Gson().toJson(new ChessGame());
        executeUpdate(statement, gameID, gameName, ChessGameJSON);
 }

 public void joinGame(int gameID, String playerColor, String username) {
        String statement;
        if (playerColor.equals("WHITE")) {
            statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
        } else {
            statement = "UPDATE games SET blackUsername=? WHERE gameID=?";
        }

        executeUpdate(statement, username, gameID);
 }

 public ArrayList<GameRecord> listAllGames() {
    ArrayList<GameRecord> gamesList = new ArrayList<>();
    var query = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games";
     try (var conn = DatabaseManager.getConnection();
          var ps = conn.prepareStatement(query)) {

         try (var rs = ps.executeQuery()) {
             while (rs.next()) {
                 Integer gameID = rs.getInt("gameID");
                 String whiteUsername = rs.getString("whiteUsername");
                 String blackUsername = rs.getString("blackUsername");
                 String gameName = rs.getString("gameName");
                 gamesList.add(new GameRecord(gameID, whiteUsername, blackUsername, gameName));
             }
         }
     } catch (SQLException | DataAccessException e) {
         System.out.println("Database error: " + e.getMessage());
     }
     return gamesList;
 }

 public void clearGames() {
    var statement = "TRUNCATE games";
    executeUpdate(statement);
 }

 private int executeUpdate(String statement, Object... params) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return 0;
    }
}
