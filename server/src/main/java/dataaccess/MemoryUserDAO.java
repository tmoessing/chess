package dataaccess;


import model.RegisterRequest;
import model.RegisterResult;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private ArrayList<String> userList = new ArrayList<>();

    public RegisterResult addUserData(RegisterRequest userData) throws DataAccessException {
        userList.add(userData.username());
        String authToken = "1";
        return new RegisterResult(userData.username(), authToken);
    }

    public void findUserDataViaUsername(String userData) throws DataAccessException {
        if (!userList.contains(userData)){throw new DataAccessException("Can not find user from userData");}
    }

    public void deleteAllUsers() throws DataAccessException{

    }
}
