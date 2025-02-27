package service;

import dataaccess.*;
import model.*;
import org.eclipse.jetty.util.log.Log;

public class UserService extends Service {

    public UserService() {
        super();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (userDataAccess.isUsernameTaken(registerRequest.username())) {
            throw new DataAccessException("Error: already taken");
        }

        userDataAccess.addUserData(registerRequest);

        String authToken = authDataAccess.createAuthToken();
        authDataAccess.addAuthData(authToken, registerRequest.username());

        return new RegisterResult(registerRequest.username(), authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if (!userDataAccess.isUsernameTaken(loginRequest.username())) {
            throw new DataAccessException("Error: unauthorized");
        }

        UserData userData = userDataAccess.pullUserData(loginRequest.username());
        if (!userData.password().equals(loginRequest.password())){
            throw new DataAccessException("Error: unauthorized");
        }

        String authToken = authDataAccess.createAuthToken();
        authDataAccess.addAuthData(authToken, loginRequest.username());
        return new LoginResult(loginRequest.username(), authToken);
    }

    public void clearUsers() {
        userDataAccess.clearAllUsers();
    }
}