package service;

import dataaccess.*;
import model.*;

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
        authDataAccess.addAuthData(registerRequest.username(), authToken);

        return new RegisterResult(registerRequest.username(), authToken);
    }

//    public LoginResult login(LoginRequest loginRequest) {
//        return dateAccess.findUserDataViaUsername(loginRequest);
//    }
//
//    public void logout(LogoutRequest logoutRequest) {}

    public void clearUsers() {
        userDataAccess.clearAllUsers();
    }
}