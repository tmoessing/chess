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

class UserServiceTest {

    private UserService userService;
    private MemoryUserDAO memoryUserDAO;
    private MemoryAuthDAO memoryAuthDAO;

    @BeforeEach
    void set_up() {
        userService = new UserService();
        memoryUserDAO = new MemoryUserDAO();
        memoryAuthDAO = new MemoryAuthDAO();

        memoryUserDAO.clearAllUsers();
        memoryAuthDAO.clearAuthData();
    }

    @Test
    void register_success() throws DataAccessException {
        String username = "username";
        RegisterRequest registerRequest = new RegisterRequest(username, "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);
        assertTrue(memoryAuthDAO.isAuthTokenExistent(registerResult.authToken()));
        assertTrue(memoryUserDAO.isUsernameTaken(username));
    }

    @Test
    void register_already_taken() throws DataAccessException {
        String username = "username";
        RegisterRequest registerRequest = new RegisterRequest(username, "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);

        RegisterRequest registerRequest1 = new RegisterRequest(username, "password", "email");
        Exception exception = assertThrows(DataAccessException.class,
                () -> {userService.register(registerRequest);});
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void login_success() throws DataAccessException{
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
    }

    @Test
    void login_failure() throws DataAccessException{
        // Register
        String username = "username";
        String password = "password";
        RegisterRequest registerRequest = new RegisterRequest(username, password, "email");
        RegisterResult registerResult = userService.register(registerRequest);
        assertTrue(memoryAuthDAO.isAuthTokenExistent(registerResult.authToken()));
        assertTrue(memoryUserDAO.isUsernameTaken(username));

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

        assertFalse(memoryUserDAO.isUsernameTaken("username"));
    }
}