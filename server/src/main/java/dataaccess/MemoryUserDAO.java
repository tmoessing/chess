package dataaccess;


import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private static final ArrayList<UserData> userList = new ArrayList<>();

    public boolean isUsernameTaken(String username) {
        for (UserData userData : userList) {
            if (userData.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void addUserData(RegisterRequest registerRequest) {
        UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        userList.add(userData);
    }

    public void findUserDataViaUsername(String userData) throws DataAccessException {
        if (!userList.contains(userData)){throw new DataAccessException("Can not find user from userData");}
    }

    public void deleteAllUsers() throws DataAccessException{
        userList.clear();
    }
}
