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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
           """
        };
        DatabaseManager.configureDatabase(createAuthsTable);
    }

    public String createAuthToken(){
        return UUID.randomUUID().toString();
    }

    public void addAuthData(String username, String authToken) {
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, authToken, username);
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
        return "";
    }

    public void removeAuthToken(String authToken) {

    }

    public void clearAuthData() {
        var statement = "TRUNCATE auths";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) { // throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
//                    else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException | DataAccessException e) {
//            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
        return 0;
    }
}
