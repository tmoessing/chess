package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {
    private SQLAuthDAO sqlAuthDAO;
    private String username = "username";
    private String authToken = "good-auth-token";

    void insertAuth() {
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authToken, username);
    }

    @BeforeEach
    void setUp() {
        sqlAuthDAO = new SQLAuthDAO();
        var statement = "TRUNCATE auths";
        DatabaseManager.executeUpdate(statement);
    }

    @Test
    void createAuthTokenSuccess() {
        assertInstanceOf(String.class, sqlAuthDAO.createAuthToken());
    }

    @Test
    void createAuthTokenFailure() {
//        Not possible
    }

    @Test
    void addAuthDataSuccess() {
        assertFalse(sqlAuthDAO.isAuthTokenExistent(authToken));
        sqlAuthDAO.addAuthData(username, authToken);
        assertTrue(sqlAuthDAO.isAuthTokenExistent(authToken));
    }

    @Test
    void addAuthDataFailure() {
        sqlAuthDAO.addAuthData(username, authToken);
        assertTrue(sqlAuthDAO.isAuthTokenExistent(authToken));
    }

    @Test
    void isAuthTokenExistentSuccess() {
        insertAuth();
        assertTrue(sqlAuthDAO.isAuthTokenExistent(authToken));
    }

    @Test
    void isAuthTokenExistentFailure() {
        assertFalse(sqlAuthDAO.isAuthTokenExistent(authToken));
    }

    @Test
    void getUsernameViaAuthTokenSuccess() {
        insertAuth();
        assertEquals(username, sqlAuthDAO.getUsernameViaAuthToken(authToken));
    }

    @Test
    void getUsernameViaAuthTokenFailure() {
        assertNull(sqlAuthDAO.getUsernameViaAuthToken(authToken));
    }

    @Test
    void removeAuthTokenSuccess() {
        insertAuth();
        sqlAuthDAO.removeAuthToken(authToken);
        assertFalse(sqlAuthDAO.isAuthTokenExistent(authToken));
    }

    @Test
    void removeAuthTokenFailure() {
    // not possible
    }

    @Test
    void clearAuthData() {
        insertAuth();
        sqlAuthDAO.clearAuthData();
        assertFalse(sqlAuthDAO.isAuthTokenExistent(authToken));
    }
}