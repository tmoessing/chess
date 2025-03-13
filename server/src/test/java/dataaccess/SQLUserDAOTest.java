package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {
    private SQLUserDAO sqlUserDAO;
    private String username = "username";
    private String password = "password";
    private String email = "email";

    void insertUser() {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, username, password, email);
    }

    @BeforeEach
    void setUp() {
       sqlUserDAO = new SQLUserDAO();
       var statement = "TRUNCATE users";
       DatabaseManager.executeUpdate(statement);
    }

    @Test
    void isUsernameTakenTrue() {
        insertUser();
        assertTrue(sqlUserDAO.isUsernameTaken(username));
    }

    @Test
    void isUsernameTakenFalse() {
        assertFalse(sqlUserDAO.isUsernameTaken(username));
    }

    @Test
    void addUserDataSuccess() {
        assertFalse(sqlUserDAO.isUsernameTaken(username));
        sqlUserDAO.addUserData(username, password, email);
        assertTrue(sqlUserDAO.isUsernameTaken(username));
    }

    @Test
    void addUserDataFailure() {
        sqlUserDAO.addUserData(username, password, null);
        assertFalse(sqlUserDAO.isUsernameTaken(username));
    }

    @Test
    void pullUserDataSuccess() {
        insertUser();
        UserData userData = sqlUserDAO.pullUserData(username);
        assertEquals(userData.username(), username);
        assertEquals(userData.password(), password);
        assertEquals(userData.email(), email);
    }

    @Test
    void pullUserDataFailure() {
        UserData userData = sqlUserDAO.pullUserData(username);
        assertNull(userData);
    }

    @Test
    void clearAllUsers() {
        insertUser();
        sqlUserDAO.clearAllUsers();

        assertFalse(sqlUserDAO.isUsernameTaken(username));
    }
}