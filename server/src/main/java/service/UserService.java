package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

public class UserService {
    private final UserDAO dateAccess;

    public UserService(UserDAO dateAccess){
        this.dateAccess = dateAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
       return dateAccess.addUserData(registerRequest);
    }

    public LoginResult login(LoginRequest loginRequest) {
//        return dateAccess.findUserDataViaUsername(loginRequest);
    }

    public void logout(LogoutRequest logoutRequest) {}
}