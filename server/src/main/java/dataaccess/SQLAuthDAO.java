package dataaccess;

import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {
        String[] createAuthsTable = {
           """
           CREATE TABLE IF NOT EXISTS auths (
              `authToken` VARCHAR(255) NOT NULL UNIQUE,
              `username` VARCHAR(255) NOT NULL,
              PRIMARY KEY (`authToken`)
            );
           """
        };
        DatabaseManager.configureDatabase(createAuthsTable);
    }

    public String createAuthToken(){
        return UUID.randomUUID().toString();
    }

    public void addAuthData(String username, String authToken) {
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authToken, username);
    }

    public boolean isAuthTokenExistent(String authToken) {
        String query = "SELECT 1 FROM auths WHERE authToken=? LIMIT 1";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {

            ps.setString(1, authToken);

            try (var rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

    public String getUsernameViaAuthToken(String authToken) {
        String query = "SELECT username FROM auths WHERE authToken=? LIMIT 1";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {

            ps.setString(1, authToken);

            try (var rs = ps.executeQuery()) {
                 if (rs.next()) {
                     return rs.getString("username");
                 }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    public void removeAuthToken(String authToken) {
        var statement = "DELETE FROM auths where authToken=?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    public void clearAuthData() {
        var statement = "TRUNCATE auths";
        DatabaseManager.executeUpdate(statement);
    }


}
