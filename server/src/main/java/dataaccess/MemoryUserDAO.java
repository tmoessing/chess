package dataaccess;


import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private ArrayList<String> userList = new ArrayList<>();

    public void addUserData(String userData) throws DataAccessException {
        userList.add(userData);
    }

    public void findUserDataViaUsername(String userData) throws DataAccessException {
        if (!userList.contains(userData)){throw new DataAccessException("Can not find user from userData");}
    }

    public void deleteAllUsers() throws DataAccessException{

    }
}
