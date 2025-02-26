package dataaccess;

import model.RegisterRequest;

public interface UserDAO {

    boolean isUsernameTaken(String userName);

    void addUserData(RegisterRequest userData);

    void findUserDataViaUsername(String userData) throws DataAccessException;

    void clearAllUsers();

}
