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
         );
         """
        };
        DatabaseManager.configureDatabase(createGamesTable);
    }

    public int createGameID() {
        String query = "SELECT COUNT(gameID) FROM games";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1)+1;
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
        var statement = "INSERT INTO games (gameID, gameName, chessGameJSON) VALUES (?, ?, ?)";
        var chessGameJSON = new Gson().toJson(new ChessGame());
        DatabaseManager.executeUpdate(statement, gameID, gameName, chessGameJSON);
    }

    public void updateGame(int gameID, ChessGame chessGame) {
        var statement = "UPDATE games SET chessGameJSON=? WHERE gameID=? VALUES (?, ?)";
        var chessGameJSON = new Gson().toJson(chessGame);
        DatabaseManager.executeUpdate(statement, chessGameJSON, gameID);
    }

    public ChessGame.TeamColor userColor(int gameID, String username) {
        String query = "SELECT whiteUsername, blackUsername FROM games WHERE gameID=? LIMIT 1";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    if (whiteUsername.equals(username)){
                        return ChessGame.TeamColor.WHITE;
                    } else if (blackUsername.equals(username)) {
                        return ChessGame.TeamColor.BLACK;
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    public void joinGame(int gameID, String playerColor, String username) {
        String statement;
        if (playerColor.equals("WHITE")) {
            statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
        } else {
            statement = "UPDATE games SET blackUsername=? WHERE gameID=?";
        }

        DatabaseManager.executeUpdate(statement, username, gameID);
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

    public ChessGame getGameBoard(int gameID) {
        ChessGame chessGame = null;
            var query = "SELECT ChessGameJSON FROM games WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    String chess = rs.getString("ChessGameJSON");
                    chessGame = new Gson().fromJson(chess, ChessGame.class);
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return chessGame;
    }

    public void clearGames() {
        var statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }
}
