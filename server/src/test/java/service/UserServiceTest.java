package service;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLUserDAO;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private SQLUserDAO sqlUserDAO;
    private SQLAuthDAO sqlAuthDAO;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        sqlUserDAO = new SQLUserDAO();
        sqlAuthDAO = new SQLAuthDAO();

        sqlUserDAO.clearAllUsers();
        sqlAuthDAO.clearAuthData();
    }

    @Test
    void registerSuccess() throws DataAccessException {
        String username = "username";
        RegisterRequest registerRequest = new RegisterRequest(username, "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);
        assertTrue(sqlAuthDAO.isAuthTokenExistent(registerResult.authToken()));
        assertTrue(sqlUserDAO.isUsernameTaken(username));
    }

    @Test
    void registerAlreadyTaken() throws DataAccessException {
        String username = "username";
        RegisterRequest registerRequest = new RegisterRequest(username, "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);

        RegisterRequest registerRequest1 = new RegisterRequest(username, "password", "email");
        Exception exception = assertThrows(DataAccessException.class,
                () -> {userService.register(registerRequest);});
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void loginSuccess() throws DataAccessException{
        // Register
        String username = "username";
        String password = "password";
        RegisterRequest registerRequest = new RegisterRequest(username, password, "email");
        RegisterResult registerResult = userService.register(registerRequest);
        assertTrue(sqlAuthDAO.isAuthTokenExistent(registerResult.authToken()));
        assertTrue(sqlUserDAO.isUsernameTaken(username));

        // Login
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.login(loginRequest);
        assertTrue(sqlAuthDAO.isAuthTokenExistent(loginResult.authToken()));
        assertTrue(sqlUserDAO.isUsernameTaken(loginRequest.username()));
    }

    @Test
    void loginFailure() throws DataAccessException{
        // Register
        String username = "username";
        String password = "password";
        RegisterRequest registerRequest = new RegisterRequest(username, password, "email");
        RegisterResult registerResult = userService.register(registerRequest);
        assertTrue(sqlAuthDAO.isAuthTokenExistent(registerResult.authToken()));
        assertTrue(sqlUserDAO.isUsernameTaken(username));

        // Login
        LoginRequest loginRequest = new LoginRequest(username, "incorrect_password");

        Exception exception = assertThrows(DataAccessException.class,
                () -> {userService.login(loginRequest);});
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void clearUsers() throws DataAccessException {
        String username = "username";
        RegisterRequest registerRequest = new RegisterRequest(username, "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);

        userService.clearUsers();

        assertFalse(sqlUserDAO.isUsernameTaken("username"));
    }
}