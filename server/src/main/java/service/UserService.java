package service;

import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserService extends Service {

    public UserService() {
        super();
    }

    private String hashPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    private boolean verifyUser(String username, String providedClearTextPassword) {
        // read the previously hashed password from the database
        var hashedPassword = userDataAccess.pullUserData(username).password();

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }


    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (userDataAccess.isUsernameTaken(registerRequest.username())) {
            throw new DataAccessException("Error: already taken");
        }
        String username = registerRequest.username();
        String password = hashPassword(registerRequest.password());
        String email = registerRequest.email();

        userDataAccess.addUserData(username, password, email);

        String authToken = authDataAccess.createAuthToken();
        authDataAccess.addAuthData(registerRequest.username(), authToken);

        return new RegisterResult(registerRequest.username(), authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if (!userDataAccess.isUsernameTaken(loginRequest.username())) {
            throw new DataAccessException("Error: unauthorized");
        }

        String username = loginRequest.username();
        String password = loginRequest.password();

        UserData userData = userDataAccess.pullUserData(username);
        if (!verifyUser(username, password)){
            throw new DataAccessException("Error: unauthorized");
        }

        String authToken = authDataAccess.createAuthToken();
        authDataAccess.addAuthData(authToken, username);
        return new LoginResult(username, authToken);
    }

    public void clearUsers() {
        userDataAccess.clearAllUsers();
    }
}