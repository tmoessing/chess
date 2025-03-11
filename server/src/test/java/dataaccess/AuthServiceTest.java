package dataaccess;

import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;
    private UserService userService;
    private SQLUserDAO sqlUserDAO;
    private SQLAuthDAO sqlAuthDAO;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        userService = new UserService();
        sqlUserDAO = new SQLUserDAO();
        sqlAuthDAO = new SQLAuthDAO();

        sqlUserDAO.clearAllUsers();
        sqlAuthDAO.clearAuthData();
    }

    @Test
    void logoutSuccess() throws DataAccessException{
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

        authService.logout(loginResult.authToken());

        assertFalse(sqlAuthDAO.isAuthTokenExistent(loginResult.authToken()));
    }

    @Test
    void logoutFailure() throws DataAccessException{

        Exception exception = assertThrows(DataAccessException.class,
                () -> {authService.logout("bad_auth");});
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void clearAuthData() throws DataAccessException {
        String username = "username";
        RegisterRequest registerRequest = new RegisterRequest(username, "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);
        assertTrue(sqlAuthDAO.isAuthTokenExistent(registerResult.authToken()));

        authService.clearAuthData();

        assertFalse(sqlAuthDAO.isAuthTokenExistent(registerResult.authToken()));
    }
}