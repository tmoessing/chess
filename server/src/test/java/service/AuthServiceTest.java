package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;
    private UserService userService;
    private MemoryUserDAO memoryUserDAO;
    private MemoryAuthDAO memoryAuthDAO;

    @BeforeEach
    void set_up() {
        authService = new AuthService();
        userService = new UserService();
        memoryUserDAO = new MemoryUserDAO();
        memoryAuthDAO = new MemoryAuthDAO();

        memoryUserDAO.clearAllUsers();
        memoryAuthDAO.clearAuthData();
    }

    @Test
    void logout_success() throws DataAccessException{
        // Register
        String username = "username";
        String password = "password";
        RegisterRequest registerRequest = new RegisterRequest(username, password, "email");
        RegisterResult registerResult = userService.register(registerRequest);
        assertTrue(memoryAuthDAO.isAuthTokenExistent(registerResult.authToken()));
        assertTrue(memoryUserDAO.isUsernameTaken(username));

        // Login
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.login(loginRequest);
        assertTrue(memoryAuthDAO.isAuthTokenExistent(loginResult.authToken()));
        assertTrue(memoryUserDAO.isUsernameTaken(loginRequest.username()));

        authService.logout(loginResult.authToken());

        assertFalse(memoryAuthDAO.isAuthTokenExistent(loginResult.authToken()));
    }

    @Test
    void logout_failure() throws DataAccessException{

        Exception exception = assertThrows(DataAccessException.class,
                () -> {authService.logout("bad_auth");});
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void clearAuthData() throws DataAccessException {
        String username = "username";
        RegisterRequest registerRequest = new RegisterRequest(username, "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);
        assertTrue(memoryAuthDAO.isAuthTokenExistent(registerResult.authToken()));

        authService.clearAuthData();

        assertFalse(memoryAuthDAO.isAuthTokenExistent(registerResult.authToken()));
    }
}