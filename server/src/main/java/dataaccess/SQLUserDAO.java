package dataaccess;

import model.UserData;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {
        String[] createUsersTable = {
           """
           CREATE TABLE IF NOT EXISTS users (
           `id` INT NOT NULL AUTO_INCREMENT,
              `username` VARCHAR(255) NOT NULL UNIQUE,
              `password` CHAR(64) NOT NULL,
              `email` VARCHAR(320) NOT NULL UNIQUE,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
           """
        };
        DatabaseManager.configureDatabase(createUsersTable);
    }

    public boolean isUsernameTaken(String username) {
        String query = "SELECT 1 FROM users WHERE username=? LIMIT 1";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {

            ps.setString(1, username);

            try (var rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

    public void addUserData(String username, String password, String email) {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, username, password, email);
    }

    public UserData pullUserData(String username) {
        String query = "SELECT username, password, email FROM users WHERE username=? LIMIT 1";

        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {

            ps.setString(1, username);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    public void clearAllUsers() {
        var statement = "TRUNCATE users";
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
        } catch (SQLException | DataAccessException ignored) {}
        return 0;
    }
}
