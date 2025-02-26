package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

public class UserService {
    private UserDAO userDataAccess;
    private AuthDAO authDataAccess;

    public UserService(UserDAO dateAccess, AuthDAO authDataAccess){
        this.userDataAccess = dateAccess;
        this.authDataAccess = authDataAccess;
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
////        return dateAccess.findUserDataViaUsername(loginRequest);
//    }
//
    public void logout(LogoutRequest logoutRequest) {}
}